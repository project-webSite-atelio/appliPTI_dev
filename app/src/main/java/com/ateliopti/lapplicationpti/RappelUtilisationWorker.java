package com.ateliopti.lapplicationpti;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class RappelUtilisationWorker extends Worker {
        private final Context context;

        public RappelUtilisationWorker(
                @NonNull Context context,
                @NonNull WorkerParameters params) {
            super(context, params);
            this.context = context;
        }

        @Override
        public Result doWork() {

            Log.i("tracer:", "Rappel utilisation Worker executed");
            // Indicate whether the work finished successfully with the Result
            OneTimeWorkRequest mywork = new OneTimeWorkRequest.Builder(RappelUtilisationWorker.class)
                    .setInitialDelay(5, TimeUnit.MINUTES)
                    .build();
            WorkManager.getInstance(this.context).enqueue(mywork);
            return Result.success();
        }
    }
