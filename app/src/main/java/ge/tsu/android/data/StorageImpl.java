package ge.tsu.android.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class StorageImpl implements Storage {

    @Override
    public void add(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = getInstance(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, new Gson().toJson(value));
        editor.commit();
    }

    @Override
    public Object getObject(Context context, String key, Class klass) {
        SharedPreferences sharedPreferences = getInstance(context);
        String data = sharedPreferences.getString(key, null);
        return data == null ? null : new Gson().fromJson(data, klass);
    }

    @Override
    public String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getInstance(context);
        return sharedPreferences.getString(key, null);
    }

    private SharedPreferences getInstance(Context context) {
        return context.getSharedPreferences("this_is_file_name", Context.MODE_PRIVATE);
    }

    @Override
    public void deleteNote(Context context, String keyofNotes){
        Object object=getObject(context,Note.NOTE_STORAGE, Note[].class);
        Note[] notes= (Note[]) object;

        ArrayList<Note> noteArrayList=new ArrayList<>();
        noteArrayList.addAll(Arrays.asList(notes));
        Iterator<Note> noteIterator=noteArrayList.iterator();
        while (noteIterator.hasNext()){
            Note note=noteIterator.next();
            if(note.getNoteId()!=null&&note.getNoteId().equals(keyofNotes)){
                noteIterator.remove();
                break;
            }
        }
        add(context,"note_storage",noteArrayList);
    }
}













