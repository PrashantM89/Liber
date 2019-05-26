package org.app.liber;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
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
import org.app.liber.model.BookshelveDataModel;

import java.util.ArrayList;
import java.util.List;

public class BookshelveFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<BookshelveDataModel> lstBookShelveBooks;
    private FloatingActionButton floatingActionButton;
    DatabaseHelper databaseHelper;
    private BSRecyclerViewAdapter recyclerViewAdapter;
    private SearchView searchView;
    Context context;
    LinearLayout emptyBookshelfMesg;
    /*
    * Delete all code
    * */
//    private CheckBox chck_select_all;
//    private Button btn_delete_all;

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
        emptyBookshelfMesg = (LinearLayout) getActivity().findViewById(R.id.empty_bookshelf_mesg);
        emptyBookshelfMesg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    private void loadDataFromDB(){
        databaseHelper = new DatabaseHelper(getContext());
        Cursor result = databaseHelper.getData();
        lstBookShelveBooks = new ArrayList<BookshelveDataModel>();

        while (result.moveToNext()){
            BookshelveDataModel b = new BookshelveDataModel();
            b.setTitle(result.getString(result.getColumnIndex(result.getColumnName(0).toString())));
            b.setAuthors(result.getString(result.getColumnIndex(result.getColumnName(1).toString())));
            b.setSmallThumbnailLink(result.getString(result.getColumnIndex(result.getColumnName(2).toString())));
            lstBookShelveBooks.add(b);
        }
        recyclerViewAdapter = new BSRecyclerViewAdapter(getContext(),lstBookShelveBooks);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        if(lstBookShelveBooks.size() == 0){
            emptyBookshelfMesg.setVisibility(View.VISIBLE);
        }else{
            emptyBookshelfMesg.setVisibility(View.GONE);
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
                final List<BookshelveDataModel> lst = filterBookshelfBooks(lstBookShelveBooks,newText);
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

    private List<BookshelveDataModel> filterBookshelfBooks(List<BookshelveDataModel> lstBooks, String query){
        query = query.toLowerCase();
        final List<BookshelveDataModel> filteredList = new ArrayList<>();
        for(BookshelveDataModel b:lstBooks){
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
