package com.flyn.smartandroid.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class SafeAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{

    private Exception mError;

    public static boolean isRunning(SafeAsyncTask<?, ?, ?> asyncTask)
    {
        return asyncTask != null && Status.RUNNING == asyncTask.getStatus();
    }

    public static boolean isFinished(SafeAsyncTask<?, ?, ?> asyncTask)
    {
        return asyncTask == null || Status.FINISHED == asyncTask.getStatus();
    }

    public static void cancelTask(AsyncTask<?, ?, ?> userTask, boolean mayInterruptIfRunning)
    {
        if (null != userTask && !userTask.isCancelled())
        {
            userTask.cancel(mayInterruptIfRunning);
        }
    }

    @Override
    public Result doInBackground(Params... params)
    {
        try
        {
            return run(params);
        } catch (Exception e)
        {
            mError = e;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onPostExecute(Result result)
    {
        if (isCancelled())
        {
            return;
        }

        try
        {
            if (mError == null)
            {
                onSuccess(result);
            } else
            {
                mError.printStackTrace();
                onFailure(mError);
            }
        } finally
        {

            if (isCancelled())
            {
                return;
            }
            onFinish();
        }
    }

    protected abstract Result run(Params... params) throws InterruptedException, ExecutionException, TimeoutException;

    public abstract void onSuccess(Result result);

    public abstract void onFailure(Exception e);

    public void onFinish()
    {

    }
}
