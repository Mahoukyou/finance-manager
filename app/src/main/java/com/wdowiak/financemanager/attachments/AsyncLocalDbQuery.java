package com.wdowiak.financemanager.attachments;

import android.os.AsyncTask;

import com.wdowiak.financemanager.R;
import com.wdowiak.financemanager.data.LoggedInUser;
import com.wdowiak.financemanager.data.LoginRepository;
import com.wdowiak.financemanager.data.Result;

public class AsyncLocalDbQuery
{
    /*
    interface JobCallback<T>
    {
        void onResult(T result);
    }

    class AsyncJob<T> extends AsyncTask<String, Void, Result<LoggedInUser>>
    {
        public AsyncJob(JobCallback<T> callback)
        {
            this.callback = callback;
        }

        @Override
        protected Result<LoggedInUser> doInBackground(String... params)
        {
            return LoginRepository.getInstance().login(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Result<LoggedInUser> result)
        {
            if (result instanceof Result.Success)
            {
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(data));
            }
            else
            {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        }

        JobCallback callback;
    }*/
}
