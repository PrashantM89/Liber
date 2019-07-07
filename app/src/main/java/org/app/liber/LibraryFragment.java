package org.app.liber;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import org.app.liber.adapter.RecyclerViewAdapter;
import org.app.liber.pojo.BookshelfPojo;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LibraryFragment extends Fragment{

    private View v;
    private RecyclerView recyclerView;
    private ArrayList<BookshelfPojo> lstLibraryBooks;
    private SearchView searchView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayout errorLayout;
    private Button tap2Refresh;
    private LiberEndpointInterface service;
    private ProgressDialog progressDialog;

    public LibraryFragment() { }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.library_fragment,container,false);
        recyclerView = (RecyclerView)v.findViewById(R.id.library_recyclerview);
        tap2Refresh = (Button)v.findViewById(R.id.tap_to_refresh_bttn_id);
        errorLayout = (LinearLayout)v.findViewById(R.id.error_layout_id);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);

        loadLibraryData();

    }

    public void loadLibraryData(){

        lstLibraryBooks = new ArrayList<>();

        Call<ArrayList<BookshelfPojo>> call = service.getBooks();
        call.enqueue(new Callback<ArrayList<BookshelfPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<BookshelfPojo>> call, Response<ArrayList<BookshelfPojo>> response) {

                if(response.code() == 200){
                    progressDialog.dismiss();
                    errorLayout.setVisibility(View.GONE);
                    recyclerViewAdapter = new RecyclerViewAdapter(getContext(),lstLibraryBooks = response.body());
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                    recyclerView.setAdapter(recyclerViewAdapter);
                }else{
                    progressDialog.dismiss();
                    errorLayout.setVisibility(View.VISIBLE);

                    tap2Refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadLibraryData();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookshelfPojo>> call, Throwable t) {
                progressDialog.dismiss();
                errorLayout.setVisibility(View.VISIBLE);
                tap2Refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadLibraryData();
                    }
                });
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem searchItem = menu.findItem(R.id.search_id);

        searchView = (SearchView)searchItem.getActionView();
        searchView.setQueryHint("Type book, author or genre");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                    final ArrayList<BookshelfPojo> lst = filterLibraryBooks(lstLibraryBooks,query);
                    recyclerViewAdapter.setLstLibraryBooks(lst);
                    recyclerViewAdapter.notifyDataSetChanged();
                    return true;
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<BookshelfPojo> lst = filterLibraryBooks(lstLibraryBooks,newText);
                recyclerViewAdapter.setLstLibraryBooks(lst);
                recyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<BookshelfPojo> filterLibraryBooks(List<BookshelfPojo> lstBooks, String query){

        final ArrayList<BookshelfPojo> filteredList = new ArrayList<>();
        for(BookshelfPojo b:lstBooks){
            final String text = b.getTitle().toLowerCase()+" "+b.getGenre().toLowerCase()+" "+b.getAuthor().toLowerCase();

            if(text.contains(query.toLowerCase())){
                filteredList.add(b);
            }
        }
        recyclerViewAdapter.animateTo(filteredList);
        return filteredList;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLibraryData();
    }

}
