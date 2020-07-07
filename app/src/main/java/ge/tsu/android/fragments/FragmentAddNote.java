package ge.tsu.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.UUID;

import ge.tsu.android.data.Note;
import ge.tsu.android.data.Storage;
import ge.tsu.android.data.StorageImpl;
import ge.tsu.android.data.User;
import ge.tsu.android.shopnotes.MainActivity;
import ge.tsu.android.shopnotes.R;

public class FragmentAddNote extends Fragment {

    public User currentUser;
    private TextView mUserName;
    private ListView mlistOfNotes;
    private NoteArrayAdapter noteArrayAdapter;
    private EditText onemoteNote;
    public static String NOTIFICATION = "ge.tsu.android.NOTIFICATION-Done";
    public static String NOTIFICATION_DATA = "DoneData";
    private static LinearLayout emptyPage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.list_note_fragment,container,false);
        onemoteNote=view.findViewById(R.id.NewNote);
        mlistOfNotes=view.findViewById(R.id.notes);
        noteArrayAdapter=new NoteArrayAdapter(getContext(),0,new ArrayList<Note>());
        mlistOfNotes.setAdapter(noteArrayAdapter);
        emptyPage=view.findViewById(R.id.empty_note_page);

        mUserName=view.findViewById(R.id.current_username);

        final Storage storage=new StorageImpl();
        final Object object=storage.getObject(this.getContext(), Note.NOTE_STORAGE,Note[].class);
        Object object1=storage.getObject(getContext(),"current_User",User.class);

        if(object1!=null){
            currentUser = (User) object1;
        }
        mUserName.setText(currentUser.getmName());

        if(object!=null){
            Note[] arrayList= (Note[]) object;

            for (Note note:arrayList) {
                if(note.getUserId().equals(currentUser.getmId())&&note.getDone()==false){
                    noteArrayAdapter.add(note);
                }
            }
        }

        if(noteArrayAdapter.isEmpty()){
            emptyPage.setVisibility(View.VISIBLE);
            mlistOfNotes.setVisibility(View.GONE);
        }

        view.findViewById(R.id.addNewNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Storage storage1=new StorageImpl();
                Object object2=storage1.getObject(getContext(),Note.NOTE_STORAGE,Note[].class);
                Object object1=storage1.getObject(getContext(),"current_User", User.class);
            User authorUser= (User) object1;
            String userExternalId=authorUser.getmId();
            Note note=new Note();
            note.setDone(false);
            note.setMtext(onemoteNote.getText().toString());
            note.setUserId(userExternalId);
            note.setDate("12");
            note.setNoteId(UUID.randomUUID().toString());
            if(object2!=null){
                Note[] noteArrayList= (Note[]) object2;
                ArrayList<Note> noteArrayList1=new ArrayList<>();
                noteArrayList1.addAll(Arrays.asList(noteArrayList));
                noteArrayList1.add(note);
                storage1.add(getContext(),Note.NOTE_STORAGE,noteArrayList1);
                noteArrayAdapter.add(note);
            }else{
                ArrayList<Note> noteArrayList=new ArrayList<>();
                noteArrayList.add(note);
                storage1.add(getContext(),Note.NOTE_STORAGE,noteArrayList);
                noteArrayAdapter.add(note);
            }
                emptyPage.setVisibility(View.GONE);
                mlistOfNotes.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.log_out_list_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Storage storage1=new StorageImpl();
                storage1.add(getContext(),"current_User",null);
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    class NoteArrayAdapter extends ArrayAdapter<Note> {

        private Context mContext;


        public NoteArrayAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects) {
            super(context, resource, objects);
            mContext=context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final Note current=getItem(position);
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=inflater.inflate(R.layout.list_note_item,parent,false);
            TextView textView=view.findViewById(R.id.each_Note);
            textView.setText(current.getMtext());


            view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current.setDone(true);
                    remove(current);
                    Storage storage=new StorageImpl();
                    Object object=storage.getObject(getContext(),Note.NOTE_STORAGE,Note[].class);
                    Note[] notes= (Note[]) object;
                    for(Note note:notes){
                        if(note.getNoteId().equals(current.getNoteId())){
                            note.setDone(true);
                        }
                    }
                    storage.add(getContext(),Note.NOTE_STORAGE,notes);

                    Intent intent=new Intent();
                    intent.setAction(NOTIFICATION);
                    intent.putExtra(NOTIFICATION_DATA, current.getNoteId());
                    getActivity().sendBroadcast(intent);
                    if(noteArrayAdapter.isEmpty()){
                        emptyPage.setVisibility(View.VISIBLE);
                        mlistOfNotes.setVisibility(View.GONE);
                    }
                    Toast.makeText(getContext(),"note is gona done",Toast.LENGTH_LONG).show();
                }
            });
            view.findViewById(R.id.share_note).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String text = current.getMtext();
                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
                        startActivity(Intent.createChooser(intent, "Share Shop List"));
                }
            });
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(current);
                    Storage storage=new StorageImpl();
                    storage.deleteNote(getContext(),current.getNoteId());
                    if(noteArrayAdapter.isEmpty()){
                        emptyPage.setVisibility(View.VISIBLE);
                        mlistOfNotes.setVisibility(View.GONE);
                    }
                    Toast.makeText(getContext(),"1 note is deleted",Toast.LENGTH_LONG).show();
                }
            });
            return view;
        }
    }
}
