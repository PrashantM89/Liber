package org.app.liber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.app.liber.adapter.BSRecyclerViewAdapter;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.helper.ToastUtil;
import org.app.liber.pojo.BookshelfPojo;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookshelveFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<BookshelfPojo> lstBookShelveBooks;
    private FloatingActionButton floatingActionButton;
    DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    private LiberEndpointInterface service;
    private BSRecyclerViewAdapter recyclerViewAdapter;
    private SearchView searchView;
    private SharedPreferences pref;
    private String userName;
    Context context;
    LinearLayout emptyBookshelfMesg;

    public BookshelveFragment() {
        super();
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.bookshelve_fragment,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView)v.findViewById(R.id.bookshelve_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = pref.getString("USER_NAME","Unknown");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.processing_bookshelf_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        emptyBookshelfMesg = (LinearLayout) getActivity().findViewById(R.id.empty_bookshelf_mesg);
        emptyBookshelfMesg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void loadDataFromDB(){

        lstBookShelveBooks = new ArrayList<BookshelfPojo>();

        if(userName == null || userName.equals("") || userName.equals("Unknown")){
            ToastUtil.showToast(getContext(),"Username in bookshelf is error: "+userName);
        }else {
            Call<ArrayList<BookshelfPojo>> call = service.getUserBooks(userName);
            call.enqueue(new Callback<ArrayList<BookshelfPojo>>() {
                @Override
                public void onResponse(Call<ArrayList<BookshelfPojo>> call, Response<ArrayList<BookshelfPojo>> response) {

                    if (response.code() == 200) {
                        progressDialog.dismiss();
                        //errorLayout.setVisibility(View.GONE);

                        recyclerViewAdapter = new BSRecyclerViewAdapter(getContext(), lstBookShelveBooks = response.body());
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                        if (lstBookShelveBooks.size() == 0) {
                            emptyBookshelfMesg.setVisibility(View.VISIBLE);
                        } else {
                            emptyBookshelfMesg.setVisibility(View.GONE);
                        }
                    } else {
                        progressDialog.dismiss();
                        //errorLayout.setVisibility(View.VISIBLE);

//                    tap2Refresh.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            loadLibraryData();
//                        }
//                    });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<BookshelfPojo>> call, Throwable t) {
                    progressDialog.dismiss();
//                errorLayout.setVisibility(View.VISIBLE);
//                tap2Refresh.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        loadLibraryData();
//                    }
//                });
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_id);
        searchView = (SearchView)searchItem.getActionView();
        searchView.setQueryHint("Type book name..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<BookshelfPojo> lst = filterBookshelfBooks(lstBookShelveBooks,newText);
                recyclerViewAdapter.setLstBSBooks(lst);
                recyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private List<BookshelfPojo> filterBookshelfBooks(List<BookshelfPojo> lstBooks, String query){
        query = query.toLowerCase();
        final List<BookshelfPojo> filteredList = new ArrayList<>();
        for(BookshelfPojo b:lstBooks){
            final String text = b.getTitle().toLowerCase();
            if(text.contains(query)){
                filteredList.add(b);
            }
        }
        return filteredList;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDB();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            loadDataFromDB();
        }
    }
}
