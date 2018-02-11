package com.jamieadkins.gwent.data;

import android.util.Log;

import com.nytimes.android.external.fs3.FSReader;
import com.nytimes.android.external.fs3.FSWriter;
import com.nytimes.android.external.fs3.PathResolver;
import com.nytimes.android.external.fs3.filesystem.FileSystem;
import com.nytimes.android.external.store3.base.Clearable;
import com.nytimes.android.external.store3.base.Persister;
import com.nytimes.android.external.store3.base.RecordProvider;
import com.nytimes.android.external.store3.base.RecordState;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okio.BufferedSource;

public final class FileSystemRecordClearingPersister<Key> implements Persister<BufferedSource, Key>,
        RecordProvider<Key>,
        Clearable<Key> {
    private final FSReader<Key> fileReader;
    private final FSWriter<Key> fileWriter;
    private final FileSystem fileSystem;
    private final PathResolver<Key> pathResolver;
    private final long expirationDuration;
    @Nonnull
    private final TimeUnit expirationUnit;

    private FileSystemRecordClearingPersister(FileSystem fileSystem, PathResolver<Key> pathResolver,
                                              long expirationDuration,
                                              @Nonnull TimeUnit expirationUnit) {
        this.fileSystem = fileSystem;
        this.pathResolver = pathResolver;
        this.expirationDuration = expirationDuration;
        this.expirationUnit = expirationUnit;
        this.fileReader = new FSReader<>(fileSystem, pathResolver);
        this.fileWriter = new FSWriter<>(fileSystem, pathResolver);
    }

    @Nonnull
    public static <T> FileSystemRecordClearingPersister<T> create(FileSystem fileSystem,
                                                                  PathResolver<T> pathResolver,
                                                                  long expirationDuration,
                                                                  @Nonnull TimeUnit expirationUnit) {
        if (fileSystem == null) {
            throw new IllegalArgumentException("root file cannot be null.");
        } else {
            return new FileSystemRecordClearingPersister<>(fileSystem, pathResolver,
                    expirationDuration, expirationUnit);
        }
    }

    @Nonnull
    public RecordState getRecordState(@Nonnull Key key) {
        return this.fileSystem.getRecordState(this.expirationUnit,
                this.expirationDuration,
                this.pathResolver.resolve(key));
    }

    @Nonnull
    @Override
    public Maybe<BufferedSource> read(@Nonnull Key key) {
        return this.fileReader.read(key);
    }

    @Nonnull
    @Override
    public Single<Boolean> write(@Nonnull Key key, @Nonnull BufferedSource bufferedSource) {
        return this.fileWriter.write(key, bufferedSource);
    }

    @Override
    public void clear(@Nonnull Key key) {
        try {
            fileSystem.deleteAll(pathResolver.resolve(key));
        } catch (IOException e) {
            Log.e("don't worry about it", ":-)");
        }
    }
}