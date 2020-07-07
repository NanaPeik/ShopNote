package ge.tsu.android.data;

import android.content.Context;

public interface Storage {

    void add(Context context, String key, Object value);

    Object getObject(Context context, String key, Class klass);

    String getString(Context context, String key);

    void deleteNote(Context context, String keyofNotes);
}
