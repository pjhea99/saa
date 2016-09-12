package com.grotesque.saa.post;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grotesque.saa.R;
import com.grotesque.saa.common.actionbar.BaseActionBar;
import com.grotesque.saa.common.api.RetrofitApi;
import com.grotesque.saa.common.widget.NavigationView;
import com.grotesque.saa.home.data.DocumentList;
import com.grotesque.saa.imgur.ImgurApi;
import com.grotesque.saa.post.adapter.RecyclerListAdapter;
import com.grotesque.saa.post.data.ContentContainer;
import com.grotesque.saa.post.data.ImageResponse;
import com.grotesque.saa.post.data.PostData;
import com.grotesque.saa.post.data.Upload;
import com.grotesque.saa.post.data.YouTubeData;
import com.grotesque.saa.post.helper.DocumentHelper;
import com.grotesque.saa.post.helper.IntentHelper;
import com.grotesque.saa.post.helper.OnPostAdapterListener;
import com.grotesque.saa.post.helper.SimpleItemTouchHelperCallback;
import com.grotesque.saa.util.NavigationUtils;
import com.grotesque.saa.util.SoftInputUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.grotesque.saa.util.LogUtils.LOGE;
import static com.grotesque.saa.util.LogUtils.makeLogTag;

/**
 * Created by 경환 on 2016-04-12.
 */
public class PostActivity extends AppCompatActivity implements OnPostAdapterListener, TextView.OnEditorActionListener, View.OnClickListener, View.OnFocusChangeListener, AttachmentFragment.AttachmentListener, BaseActionBar.OnActionBarListener, NavigationView.OnButtonClickListener {
    private final String TAG = makeLogTag(PostActivity.class);

    private String mSection ="freeboard3";

    private Upload upload; // Upload object containging image and meta data
    private File chosenFile; //chosen file from intent

    private BaseActionBar mActionBar;
    private EditText mSearchEditText;
    private ImageButton mImageUpload;
    private ImageButton mAddMore;
    private ImageButton mCloseAll;
    private ImageButton mCancelSearch;
    private RecyclerView mRecyclerView;
    private LinearLayout mPostLayout;
    private LinearLayout mSearchLayout;
    private FrameLayout mFragmentLayout;
    private RelativeLayout mProgressView;

    private NavigationView mNavigationView;

    private AttachmentFragment mAttachmentFragment;

    private RecyclerListAdapter mAdapter;
    private ArrayList<PostData> mDatas = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mActionBar = (BaseActionBar) findViewById(R.id.actionBar);
        mActionBar.setOnActionBarListener(this);

        mSearchEditText = (EditText) findViewById(R.id.et_search_text);
        mImageUpload = (ImageButton) findViewById(R.id.imgbtn_add_photo);
        mAddMore = (ImageButton) findViewById(R.id.imgbtn_add_more);
        mCloseAll = (ImageButton) findViewById(R.id.ib_close_all);
        mCancelSearch = (ImageButton) findViewById(R.id.ib_clear_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.post_list);
        mPostLayout = (LinearLayout) findViewById(R.id.ll_post_layout);
        mSearchLayout = (LinearLayout) findViewById(R.id.ll_search_item_layout);
        mFragmentLayout = (FrameLayout) findViewById(R.id.attachment_search_fragment);
        mProgressView = (RelativeLayout) findViewById(R.id.loading_progress);

        mNavigationView = (NavigationView) findViewById(R.id.navigationView);
        mNavigationView.setOnButtonClickListener(this);

        mSearchEditText.setOnEditorActionListener(this);
        mSearchEditText.setOnFocusChangeListener(this);
        mImageUpload.setOnClickListener(this);
        mAddMore.setOnClickListener(this);
        mCloseAll.setOnClickListener(this);
        mCancelSearch.setOnClickListener(this);

        mAdapter = new RecyclerListAdapter(this, mDatas, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mDatas.add(new PostData("title", ""));
    }
    private Fragment getFragment(int paramInt)
    {
        return getFragmentManager().findFragmentById(paramInt);
    }

    public void onChooseImage() {
        IntentHelper.chooseFileIntent(this);
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
        upload.title = "";
        upload.description = "";
    }
    public void uploadImage() {
    /*
      Create the @Upload object
     */
        if (chosenFile == null) return;
        createUpload(chosenFile);

    /*
      Start upload
     */
        mProgressView.setVisibility(View.VISIBLE);
        ImgurApi.uploadImage(upload, new UiCallback());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == IntentHelper.POST_PREVIEW) {
            if (resultCode != RESULT_OK) {
                return;
            }
            String document_srl = data.getExtras().getString("document_srl");
            HashMap<String, String> query = new HashMap<>();
            query.put("mid", mSection);
            query.put("document_srl", document_srl);

            RetrofitApi.getInstance().getPostBoardContent(query).enqueue(new Callback<ContentContainer>() {
                @Override
                public void onResponse(Call<ContentContainer> call, Response<ContentContainer> response) {
                    if(response.body().getMessage().equals("success")){
                        ArrayList<DocumentList> arrayList = new ArrayList<>();
                        arrayList.add(response.body().getDocumentList());
                        NavigationUtils.goContentActivity(PostActivity.this, mSection, arrayList, 0);
                    }
                }

                @Override
                public void onFailure(Call<ContentContainer> call, Throwable t) {

                }
            });


        }else if(requestCode == IntentHelper.FILE_PICK){
            if (resultCode != RESULT_OK) {
                return;
            }
            Uri returnUri;
            returnUri = data.getData();
            String filePath = DocumentHelper.getPath(this, returnUri);
            //Safety check to prevent null pointer exception
            if (filePath == null || filePath.isEmpty()) return;
            chosenFile = new File(filePath);


            uploadImage();
        }
    }

    public void goPreview(ArrayList<PostData> postDatas, String mid) {
        IntentHelper.previewIntent(this, postDatas, mid);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFocusChanged(View v, boolean hasFocus) {
        if(hasFocus) {
            SoftInputUtils.show(v);
            hideAttachLayout();
        }
        else{
            SoftInputUtils.hide(v);
            showAttachLayout();
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
    public void onImageUploaded(String link){
        mDatas.add(new PostData("img", link));
        mDatas.add(new PostData("txt", ""));
        mAdapter.notifyDataSetChanged();
    }
    public void showAttachLayout(){
        if(getFragment(R.id.attachment_search_fragment) != null)
            mFragmentLayout.setVisibility(View.VISIBLE);
        if(mSearchEditText.getText().toString().isEmpty()) {
            mSearchEditText.requestFocus();
        }
    }
    public void hideAttachLayout(){
        mFragmentLayout.setVisibility(View.GONE);
    }
    public void hideAll(){
        mSearchLayout.setVisibility(View.GONE);
        hideAttachLayout();
        mAddMore.setSelected(false);
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        mFragmentLayout.setVisibility(View.VISIBLE);
        AttachmentFragment attachmentFragment = (AttachmentFragment) getFragment(R.id.attachment_search_fragment);
        if(attachmentFragment != null) {
            LOGE(TAG, "attachmentFragment not null");
            attachmentFragment.searchYoutube(v.getText().toString());
        } else {
            LOGE(TAG, "attachmentFragment null");
            AttachmentFragment attachmentFragment1 = AttachmentFragment.newInstance(v.getText().toString());
            getFragmentManager().beginTransaction().add(R.id.attachment_search_fragment, attachmentFragment1).commit();

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgbtn_add_photo:
                onChooseImage();
                break;
            case R.id.imgbtn_add_more:
                if(mSearchLayout.getVisibility() == View.VISIBLE){
                    mSearchLayout.setVisibility(View.GONE);
                    hideAttachLayout();
                    mAddMore.setSelected(false);
                }else {
                    mSearchLayout.setVisibility(View.VISIBLE);
                    showAttachLayout();
                    mAddMore.setSelected(true);
                }
                break;
            case R.id.ib_close_all:
                mSearchLayout.setVisibility(View.GONE);
                hideAttachLayout();
                mAddMore.setSelected(false);
                break;
            case R.id.ib_clear_text:
                mSearchEditText.setText("");
                break;
       }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            SoftInputUtils.show(v);
            hideAttachLayout();
        }
        else {
            SoftInputUtils.hide(v);
            showAttachLayout();
        }
    }

    @Override
    public void addVideo(YouTubeData.Item videoItem) {
        mDatas.add(new PostData("video", videoItem.getId().getVideoId(), videoItem.getSnippet().getTitle(), videoItem.getSnippet().getDescription(), videoItem.getSnippet().getThumbnails().getHigh().getUrl()));
        mDatas.add(new PostData("txt", ""));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleTextClicked() {
        mNavigationView.setVisibility(View.VISIBLE);
        mPostLayout.setVisibility(View.GONE);
    }

    @Override
    public void onLeftButtonClicked() {
        finish();
    }

    @Override
    public void onRightButtonClicked() {
        goPreview(mDatas, "freeboard3");
    }

    @Override
    public void onClicked(String mid) {
        mSection = mid;
        mNavigationView.setVisibility(View.GONE);
        mPostLayout.setVisibility(View.VISIBLE);
    }

    private class UiCallback implements Callback<ImageResponse> {
        @Override
        public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
            LOGE(TAG, response.body().data.link);
            mProgressView.setVisibility(View.GONE);
            onImageUploaded(response.body().data.link);

        }

        @Override
        public void onFailure(Call<ImageResponse> call, Throwable t) {

        }
    }
}
