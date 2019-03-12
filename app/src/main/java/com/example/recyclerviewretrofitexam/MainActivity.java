package com.example.recyclerviewretrofitexam;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.post_recycler_view);
        final PostRecyclerView adapter = new PostRecyclerView();
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://koreanjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        KoreanJsonService service = retrofit.create(KoreanJsonService.class);

        service.listPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> posts = response.body();

                Log.d(TAG, "onResponse: " + posts);

                if (posts != null) {
                    adapter.setItems(posts);
                    adapter.notifyItemRangeInserted(0, posts.size());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    private static class PostRecyclerView extends RecyclerView.Adapter<PostRecyclerView.PostViewHolder> {

        private List<Post> mItems = new ArrayList<>();

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
            return new PostViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
            Post post = mItems.get(i);

            postViewHolder.titleTextView.setText(post.getTitle());
            postViewHolder.contentTextView.setText(post.getContent());
            postViewHolder.createTextView.setText(post.getCreatedAt());
            postViewHolder.updateTextView.setText(post.getUpdatedAt());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setItems(List<Post> items) {
            mItems = items;
        }

        public static class PostViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView contentTextView;
            TextView createTextView;
            TextView updateTextView;

            public PostViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.title_text_view);
                contentTextView = itemView.findViewById(R.id.content_text_view);
                createTextView = itemView.findViewById(R.id.create_time_text_view);
                updateTextView = itemView.findViewById(R.id.updated_time_text_view);
            }
        }
    }
}
