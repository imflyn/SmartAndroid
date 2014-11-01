package com.flyn.smartandroid.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;

import java.util.List;

public class FixedOnActivityResultBugFragment extends Fragment
{

    private final SparseIntArray mRequestCodes = new SparseIntArray();

    public void registerRequestCode(int requestCode, int id)
    {
        mRequestCodes.put(requestCode, id);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        if (getParentFragment() instanceof FixedOnActivityResultBugFragment)
        {
            ((FixedOnActivityResultBugFragment) getParentFragment()).registerRequestCode(requestCode, hashCode());
            getParentFragment().startActivityForResult(intent, requestCode);
        } else
        {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!checkNestedFragmentsForResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected boolean checkNestedFragmentsForResult(int requestCode, int resultCode, Intent data)
    {
        final int id = mRequestCodes.get(requestCode);
        if (id == 0)
        {
            return false;
        }

        mRequestCodes.delete(requestCode);

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments == null)
        {
            return false;
        }

        for (Fragment fragment : fragments)
        {
            if (fragment.hashCode() == id)
            {
                fragment.onActivityResult(requestCode, resultCode, data);
                return true;
            }
        }

        return false;
    }

}