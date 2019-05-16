package org.app.liber;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.app.liber.adapter.RecyclerViewAdapter;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.model.LibraryDataModel;
import org.app.liber.pojo.BookshelfPojo;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.ProgressDialog;

public class LibraryFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    //private ArrayList<LibraryDataModel> lstLibraryBooks;
    private ArrayList<BookshelfPojo> lstLibraryBooks;
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    private SearchView searchView;
    private RecyclerViewAdapter recyclerViewAdapter;

    public LibraryFragment() { }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.library_fragment,container,false);
        recyclerView = (RecyclerView)v.findViewById(R.id.library_recyclerview);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(getContext());
        //Cursor result = databaseHelper.getLibraryData();
        lstLibraryBooks = new ArrayList<>();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.processing_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

//        while (result.moveToNext()){
//            LibraryDataModel b = new LibraryDataModel(result.getString(result.getColumnIndex(result.getColumnName(0).toString())),result.getString(result.getColumnIndex(result.getColumnName(1).toString())),result.getString(result.getColumnIndex(result.getColumnName(2).toString())),result.getString(result.getColumnIndex(result.getColumnName(3).toString())),result.getString(result.getColumnIndex(result.getColumnName(4).toString())),result.getString(result.getColumnIndex(result.getColumnName(5).toString())));
//            lstLibraryBooks.add(b);
//        }

        LiberEndpointInterface service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        Call<ArrayList<BookshelfPojo>> call = service.getBooks();

        call.enqueue(new Callback<ArrayList<BookshelfPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<BookshelfPojo>> call, Response<ArrayList<BookshelfPojo>> response) {
                recyclerViewAdapter = new RecyclerViewAdapter(getContext(),lstLibraryBooks = response.body());
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                recyclerView.setAdapter(recyclerViewAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<BookshelfPojo>> call, Throwable t) {
                System.out.println("---------------------- "+t.getMessage());
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                //final ArrayList<LibraryDataModel> lst = filterLibraryBooks(lstLibraryBooks,newText);
               //recyclerViewAdapter.setLstLibraryBooks(lst);
               // recyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<LibraryDataModel> filterLibraryBooks(List<LibraryDataModel> lstBooks, String query){
        query = query.toLowerCase();
        final ArrayList<LibraryDataModel> filteredList = new ArrayList<>();
        for(LibraryDataModel b:lstBooks){
            final String text = b.getBookTitle().toLowerCase();
            if(text.contains(query)){
                filteredList.add(b);
            }
        }
        recyclerViewAdapter.animateTo(filteredList);
        return filteredList;
    }
}
