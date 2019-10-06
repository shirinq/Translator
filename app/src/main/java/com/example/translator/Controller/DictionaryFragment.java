package com.example.translator.Controller;


import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.translator.Model.Word;
import com.example.translator.R;
import com.example.translator.Repository.WordRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DictionaryFragment extends Fragment {

    public static final int EDIT_REQUEST_CODE = 0;
    public static final int ADD_REQUEST_CODE = 1;
    public static final String ADD_DIALOG_TAG = "Add_dialog";
    public static final String EDIT_DIALOG_TAG = "Edit_dialog";

    private WordAdapter mAdapter;
    private RecyclerView mRecycler;
    private View mView;
    private Button mDone;
    private Button mDiscard;

    private List<Word> mWords;
    private Word currentWord;
    private WordRepository repository;
    private List<Word> selectedWords = new ArrayList<>();


    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance() {
        return new DictionaryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dictionary, container, false);

        mDone = mView.findViewById(R.id.done);
        mDone.setVisibility(View.GONE);

        mDiscard = mView.findViewById(R.id.discard);
        mDiscard.setVisibility(View.GONE);

        mRecycler = mView.findViewById(R.id.recyclerWord);

        repository = WordRepository.getInstance(getActivity().getApplicationContext());
        mWords = repository.getWordList();

        Recycler();
        setHasOptionsMenu(true);

        return mView;
    }

    /**
     * RECYCLER
     */
    private void Recycler() {
        mAdapter = new WordAdapter();
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * VIEW HOLDER
     */
    private class WordHolder extends RecyclerView.ViewHolder {

        private CheckBox hSelect;
        private TextView hTitle;
        private TextView hTranslation;
        private View hView;

        public WordHolder(@NonNull View itemView) {
            super(itemView);
            hSelect = itemView.findViewById(R.id.select);
            hTitle = itemView.findViewById(R.id.Title);
            hTranslation = itemView.findViewById(R.id.Translation);
            hView = itemView;
        }

        private void bind(final Word word, int visible) {
            hTitle.setText(word.getTitle());
            hTranslation.setText(word.getTranslation());
            hSelect.setVisibility(visible);
            hSelect.setChecked(false);

            hView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DictionaryFragment.this.
                            dialogBuilder(true, word, EDIT_REQUEST_CODE, EDIT_DIALOG_TAG);
                    currentWord = word;
                    return true;
                }
            });

            hSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectedWords.add(word);
                    } else {
                        if (selectedWords.contains(word))
                            selectedWords.remove(word);
                    }
                }
            });
        }
    }

    /**
     * ADAPTER
     */
    private class WordAdapter extends RecyclerView.Adapter<WordHolder> {

        private int visibility = View.GONE;

        @NonNull
        @Override
        public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext().getApplicationContext());
            View view = inflater.inflate(R.layout.word_holder, parent, false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordHolder holder, int position) {
            holder.bind(mWords.get(position), visibility);

        }

        @Override
        public int getItemCount() {
            return mWords.size();
        }

        public void setVisibility(int visibility) {
            this.visibility = visibility;
        }
    }

    private void updateUI(int visibility) {
        if (mAdapter != null) {
            mDone.setVisibility(visibility);
            mDiscard.setVisibility(visibility);
            mAdapter.setVisibility(visibility);
            mWords = repository.getWordList();
            mAdapter.notifyDataSetChanged();

        }
    }

    /**
     * RESPOND
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String title = data.getStringExtra(detailDialogFragment.EXTRA_TITLE);
            String translation = data.getStringExtra(detailDialogFragment.EXTRA_TRANSLATION);

            if (!title.equals("") && !translation.equals("")) {
                currentWord.setTitle(title);
                currentWord.setTranslation(translation);
                repository.updateWord(currentWord);
                updateUI(View.GONE);
            }
        } else if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Word word = new Word();
            String title = data.getStringExtra(detailDialogFragment.EXTRA_TITLE);
            String translation = data.getStringExtra(detailDialogFragment.EXTRA_TRANSLATION);

            word.setTitle(title);
            word.setTranslation(translation);

            repository.insertWord(word);

            updateUI(View.GONE);
        }
    }



    /**
     * MENU HANDLER
     */


    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Word> temp = new ArrayList<>();
                mWords = repository.getWordList();
                for (Word word : mWords) {
                    if (word.getTitle().equals(s) || word.getTranslation().equals(s))
                        temp.add(word);
                }
                mWords = temp;
                mAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Word> temp = new ArrayList<>();
                mWords = repository.getWordList();
                for (Word word : mWords) {
                    if (word.getTitle().contains(s)|| word.getTranslation().contains(s))
                        temp.add(word);
                }
                mWords = temp;
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateUI(View.GONE);
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_add:
                dialogBuilder(true, new Word(), ADD_REQUEST_CODE, ADD_DIALOG_TAG);
                return true;

            case R.id.menu_delete:
                updateUI(View.VISIBLE);
                btnsListener(getString(R.string.delete));
                return true;

            case R.id.menu_share:
                updateUI(View.VISIBLE);
                btnsListener(getString(R.string.share));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * detailDialog BUILDER
     *
     * @param hasChanges
     * @param word
     * @param reqCode
     */
    private void dialogBuilder(boolean hasChanges, Word word, int reqCode, String TAG) {
        detailDialogFragment dialog =
                detailDialogFragment.newInstance(hasChanges, word);
        dialog.setTargetFragment(DictionaryFragment.this, reqCode);
        dialog.show(getActivity().getSupportFragmentManager(), TAG);
    }

    private void btnsListener(final String text) {
        mDone.setText(text);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.equals(getString(R.string.delete))) {
                    for (Word w : selectedWords)
                        repository.deleteWord(w);
                }
                else if(text.equals(getString(R.string.share))){
                    String textMessage = "";
                    for (Word w : selectedWords)
                        textMessage+=(w.getTitle()+"="+w.getTranslation()+'\n');
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                    sendIntent.setType("text/plain");

                    if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                }
                selectedWords = new ArrayList<>();
                updateUI(View.GONE);
            }
        });

        mDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedWords = new ArrayList<>();
                updateUI(View.GONE);
            }
        });
    }
}
