package com.gnoemes.bubblenotes.repo.local;

import com.gnoemes.bubblenotes.repo.model.Comment;
import com.gnoemes.bubblenotes.repo.model.Description;
import com.gnoemes.bubblenotes.repo.model.Note;
import com.gnoemes.bubblenotes.repo.model.Note_;
import com.gnoemes.bubblenotes.util.CommonUtils;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.Property;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by kenji1947 on 28.09.2017.
 */

public class LocalRepositoryImpl implements LocalRepository {
    private BoxStore boxStore;

    private Box<Note> noteBox;
    private Box<Comment> commentBox;
    private Box<Description> descriptionBox;

    private PublishSubject<Boolean> subjectNoteForeignChangesStatus;

    public LocalRepositoryImpl(BoxStore boxStore) {
        this.boxStore = boxStore;

        noteBox = boxStore.boxFor(Note.class);
        commentBox = boxStore.boxFor(Comment.class);
        descriptionBox = boxStore.boxFor(Description.class);

        subjectNoteForeignChangesStatus = PublishSubject.create();

        //clearAllEntities();
    }

    private void clearAllEntities() {
        noteBox.removeAll();
        commentBox.removeAll();;
        descriptionBox.removeAll();
    }

    @Override
    public Observable<Boolean> observeNoteForeignChangesStatus() {
        //Timber.d("observeNoteForeignChangesStatus");
        return subjectNoteForeignChangesStatus;
    }

    @Override
    public Observable<List<Comment>> getAllComments() {
        Timber.d("getAllComments");
        //Timber.d("Thread: " + Thread.currentThread().getName());
        //CommonUtils.longOperation();

        Query<Comment> commentQuery = commentBox.query().build();
        return RxQuery.observable(commentQuery);
    }

    @Override
    public Observable<List<Description>> getAllDescription() {
        Timber.d("getAllDescription");
        //Timber.d("Thread: " + Thread.currentThread().getName());
        //CommonUtils.longOperation();

        Query<Description> descriptionQuery = descriptionBox.query().build();
        return RxQuery.observable(descriptionQuery);
    }

    @Override
    public Observable<List<Note>> getAllNotesSorted(Property property) {
        Timber.d("getAllNotesSorted");
        //Timber.d("Thread: " + Thread.currentThread().getName());
        //CommonUtils.longOperation();

        Query<Note> query = noteBox.query()
                .orderDesc(property)
                .eager(Note_.description, Note_.comments)
                .build();
        return RxQuery.observable(query);
    }

    @Override
    public List<Note> getAllNotesSortedList(Property property) {
        Timber.d("getAllNotesSortedList");
        Timber.d("Thread: " + Thread.currentThread().getName());
        CommonUtils.longOperation();

        Query<Note> query = noteBox.query()
                .orderDesc(property)
                .eager(Note_.description, Note_.comments)
                .build();
        return query.find();
    }


    @Override
    public Observable<Note> getNote(long id) {
        Timber.d("getNote");
        return Observable.<Note>fromCallable(() -> {

            Timber.d("Thread: " + Thread.currentThread().getName());
            CommonUtils.longOperation();

            return noteBox.get(id);});
    }

    //Создание новой Note
    @Override
    public Observable<Long> addNote(Note note) {
        Timber.d("addNote");
        subjectNoteForeignChangesStatus.onNext(false);
        return Observable.fromCallable(() -> {

            Timber.d("Thread: " + Thread.currentThread().getName());
            CommonUtils.longOperation();

            return noteBox.put(note);
        });
    }

    //Каскадное сохранение.


    //Обновление сохраненной Note.
    //Каскадное сохранение дочерних сущностей произойдет при добавлении новой сущности(без id).
    //При удалении дочерней сущности из связи, последняя будет отвязана от родительской но останется в бд.
    //Для сохранения изменения в дочерних нужных сохранять их явно
    @Override
    public Observable<Long> UpdateNote(Note note) {
        Timber.d("UpdateNote");
        subjectNoteForeignChangesStatus.onNext(false);
        return Observable.fromCallable(() -> {

            Timber.d("Thread: " + Thread.currentThread().getName());
            CommonUtils.longOperation();

            boxStore.runInTx(() -> {

                //явное сохранение изменений в дочерней сущности
                descriptionBox.put(note.getDescription().getTarget());

                noteBox.put(note);

                //TODO Уродское удаление unmanaged relation
                List<Comment> list = commentBox.getAll();
                for (Comment c: list) {
                    if (c.getNoteToOne().getTarget() == null) {
                        commentBox.remove(c);
                    }
                }
            });
            return note.getId();
        });

    }

    @Override
    public Observable<Boolean> deleteNote(long id) {
        Timber.d("deleteNote");
        subjectNoteForeignChangesStatus.onNext(false);
        return Observable.fromCallable(() -> {

            Timber.d("Thread: " + Thread.currentThread().getName());
            CommonUtils.longOperation();

            boxStore.runInTx(() -> {
                Note note = noteBox.get(id);

                descriptionBox.remove(note.getDescription().getTargetId());
                commentBox.remove(note.getComments());
                noteBox.remove(id);
            });
            return true;
        });
    }


}

