package com.thing.quoter;

import android.databinding.ObservableArrayMap;
import android.databinding.ObservableMap;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.ParameterizedType;

public class FirestoreList<T> extends ObservableArrayMap<T, String> {

    private CollectionReference collectionReference;
    private Class<T> classType;

    public FirestoreList(Class<T> classType, CollectionReference collectionReference){
        this.classType = classType;
        this.collectionReference = collectionReference;
        addOnMapChangedCallback(new OnMapChangedCallback<ObservableMap<T, String>, T, String>() {
            @Override
            public void onMapChanged(ObservableMap<T, String> sender, T key) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    public FirestoreList(CollectionReference collectionReference) {
        this.classType = ((Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
        this.collectionReference = collectionReference;
    }

    public void populate(int size) {
        collectionReference.limit(size).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                this.add(documentSnapshot);
            }
        });
    }

    public void add(T obj) {
        collectionReference.add(obj).addOnSuccessListener(documentReference -> {
            documentReference.getId();
            put(obj, documentReference.getId());
        });
    }

    public void add(DocumentSnapshot documentSnapshot) {
        put(documentSnapshot.toObject(classType), documentSnapshot.getId());
    }

    public void delete(Object obj) {
        String id = get(obj);
        collectionReference.document(id).delete().addOnSuccessListener(aVoid -> {
            this.remove(obj);
        });
    }

}
