package droidninja.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import droidninja.filepicker.fragments.DocFragment;
import droidninja.filepicker.fragments.DocPickerFragment;
import droidninja.filepicker.utils.FragmentUtil;
import droidninja.filepicker.utils.Orientation;

import java.util.ArrayList;

public class FilePickerActivity extends AppCompatActivity
        implements DocFragment.DocFragmentListener, DocPickerFragment.DocPickerFragmentListener {

    private static final String TAG = FilePickerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(PickerManager.getInstance().getTheme());
        setContentView(R.layout.activity_file_picker);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set orientation
        Orientation orientation = PickerManager.getInstance().getOrientation();
        if (orientation == Orientation.PORTRAIT_ONLY) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation == Orientation.LANDSCAPE_ONLY) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        initView();
    }

    private void initView() {
        setToolbarTitle(PickerManager.getInstance().getCurrentCount());

        if (PickerManager.getInstance().isDocSupport()) {
            PickerManager.getInstance().addDocTypes();
        }

        DocPickerFragment photoFragment = DocPickerFragment.newInstance();
        FragmentUtil.addFragment(this, R.id.container, photoFragment);
    }

    private void setToolbarTitle(int count) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int maxCount = PickerManager.getInstance().getMaxCount();
            if (maxCount == -1 && count > 0) {
                actionBar.setTitle(String.format(getString(R.string.attachments_num), count));
            } else if (maxCount > 0 && count > 0) {
                actionBar.setTitle(
                        String.format(getString(R.string.attachments_title_text), count, maxCount));
            } else if (!TextUtils.isEmpty(PickerManager.getInstance().getTitle())) {
                actionBar.setTitle(PickerManager.getInstance().getTitle());
            } else {
                actionBar.setTitle(R.string.select_doc_text);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_done);
        if (menuItem != null) {
            if (PickerManager.getInstance().getMaxCount() == 1) {
                menuItem.setVisible(false);
            } else {
                menuItem.setVisible(true);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {
            returnData(PickerManager.getInstance().getSelectedFiles());
            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PickerManager.getInstance().reset();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_MEDIA_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    returnData(PickerManager.getInstance().getSelectedFiles());
                } else {
                    setToolbarTitle(PickerManager.getInstance().getCurrentCount());
                }
                break;
        }
    }

    private void returnData(ArrayList<String> paths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS, paths);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected() {
        int currentCount = PickerManager.getInstance().getCurrentCount();
        setToolbarTitle(currentCount);

        if (PickerManager.getInstance().getMaxCount() == 1 && currentCount == 1) {
            returnData(PickerManager.getInstance().getSelectedFiles());
        }
    }
}
