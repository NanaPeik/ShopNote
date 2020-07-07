package ge.tsu.android.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ge.tsu.android.data.Note;
import ge.tsu.android.data.Storage;
import ge.tsu.android.data.StorageImpl;
import ge.tsu.android.data.User;
import ge.tsu.android.shopnotes.MainActivity;
import ge.tsu.android.shopnotes.R;

public class FragmentDoneNote extends Fragment {

    private User currentUser;
    private static ListView listView;
    private static DoneNoteArrayAdapter doneNoteArrayAdapter;
    private FragmentNoteCatcherBroadcast fragmentNoteCatcherBroadcast;
    private static LinearLayout emptyPage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.done_notes_fragment,container,false);
        listView=view.findViewById(R.id.done_note_list);
        doneNoteArrayAdapter=new DoneNoteArrayAdapter(getContext(),0, new ArrayList<Note>());
        listView.setAdapter(doneNoteArrayAdapter);
        emptyPage=view.findViewById(R.id.empty_note_page_done);
        final Storage storage=new StorageImpl();
        Object object=storage.getObject(getContext(),Note.NOTE_STORAGE,Note[].class);
        if(object!=null){
            Note[] notes= (Note[]) object;
            for(Note note:notes){
                currentUser= (User) storage.getObject(getContext(),"current_User",User.class);

                if(note.getDone()==true&&note.getUserId().equals(currentUser.getmId())){
                    doneNoteArrayAdapter.add(note);
                }
            }
        }
        if(doneNoteArrayAdapter.isEmpty()){
            emptyPage.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        initBroadcast();
        view.findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage storage1=new StorageImpl();
                storage1.add(getContext(),"current_User",null);
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.delete_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage storage2=new StorageImpl();
                Object object1=storage2.getObject(getContext(),Note.NOTE_STORAGE,Note[].class);

                if(object1!=null){
                    Note[] notes= (Note[]) object1;
                    ArrayList<Note> noteArrayList=new ArrayList<>();
                    noteArrayList.addAll(Arrays.asList(notes));
                    for(Note note : notes){
                        if(note.getDone()&&currentUser.getmId().equals(note.getUserId())){
                            noteArrayList.remove(note);
                        }
                    }
                    storage2.add(getContext(),Note.NOTE_STORAGE,noteArrayList);
                    doneNoteArrayAdapter.clear();
                }
                emptyPage.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
        return view;
    }


    void initBroadcast(){
        fragmentNoteCatcherBroadcast=new FragmentNoteCatcherBroadcast();
        IntentFilter filter=new IntentFilter();
        filter.addAction(FragmentAddNote.NOTIFICATION);
        getActivity().registerReceiver(fragmentNoteCatcherBroadcast,filter);

    }

    public static class FragmentNoteCatcherBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(FragmentAddNote.NOTIFICATION_DATA)) {
                String dataText = intent.getStringExtra(FragmentAddNote.NOTIFICATION_DATA);
                Storage storage=new StorageImpl();
                Object object=storage.getObject(context,Note.NOTE_STORAGE,Note[].class);
                if(object!=null){
                    Note[] notes= (Note[]) object;
                    for(Note note:notes){
                        if(note.getNoteId().equals(dataText)){
                            doneNoteArrayAdapter.add(note);
                            emptyPage.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    class DoneNoteArrayAdapter extends ArrayAdapter<Note> {

        private Context mContext;

        public DoneNoteArrayAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
            super(context, resource, objects);
            mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Note current=getItem(position);
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=inflater.inflate(R.layout.done_note_item,parent,false);
            TextView textView=view.findViewById(R.id.each_done_Note);
            textView.setText(current.getMtext());

            view.findViewById(R.id.delete_done_note).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(current);
                    Storage storage=new StorageImpl();
                    storage.deleteNote(getContext(),current.getNoteId());
                    Toast.makeText(getContext(),"1 note is deleted",Toast.LENGTH_LONG).show();
                    if(doneNoteArrayAdapter.isEmpty()){
                        emptyPage.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }
            });
            return view;
        }
    }
}
