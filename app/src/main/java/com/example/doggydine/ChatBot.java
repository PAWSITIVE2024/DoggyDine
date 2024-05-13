package com.example.doggydine;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class ChatBot extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    ImageButton bottomButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json");
    //OkHttpClient client = new OkHttpClient();
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_bot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.chat_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.meeage_edit_text);
        sendButton = findViewById(R.id.send_btn);
        //setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        sendButton.setOnClickListener((v)->{
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
        });
        client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        bottomButton = findViewById(R.id.sheet_btn);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }
    void addToChat(String message, String sentBy){
        welcomeTextView.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    void addToChat(String message, String sentBy, ImageView imageView){
        welcomeTextView.setVisibility(View.GONE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy, imageView));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String question) {
        messageList.add(new Message("... ", Message.SENT_BY_BOT));
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try{
            baseAi.put("role", "user");
            baseAi.put("content", "You are kind and sweet AI Assistant. I'm going to ask you about dogs. Please answer in Korean");
            userMsg.put("role", "user");
            userMsg.put("content", question);
            arr.put(baseAi);
            arr.put(userMsg);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();
        try{
            object.put("model", "gpt-3.5-turbo");
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", question);
            JSONArray messagesArray = new JSONArray();
            messagesArray.put(messageObj);
            object.put("messages", messagesArray);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(object.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    addResponse("Failed to load response due to "+response.body().string());
                }
            }
        });
    }
    private void showBottomDialog(){
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout asklayout = dialog.findViewById(R.id.how_to_ask);
        LinearLayout firstlayout = dialog.findViewById(R.id.doggyfruit);
        LinearLayout secondlayout = dialog.findViewById(R.id.doggyallergy);
        ImageView downButton = dialog.findViewById(R.id.down_btn);

        asklayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                String text = "어떻게 질문하면 되나요?";
                String response = "이 페이지에서는 무엇이든 자유롭게 물어볼 수 있습니다! \n강아지한테 바나나 줘도 돼? 라고 물어보세요!";
                addToChat(text, Message.SENT_BY_ME);
                addToChat(response, Message.SENT_BY_BOT);
            }
        });
        firstlayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                String text = "강아지가 먹어도 되는 과일 알려주세요.";
                String response = "강아지에게 다양한 과일을 줘도 괜찮습니다. \n다만 항상 적정량만 급여해야 한다는 것을 잊지 마세요! \n만약 얼마나 줘야 하는지 궁금하면 채팅으로 물어보세요!🙂\n 예) 5kg 소형견한테 사과 얼마나 줘야 해?";
                addToChat(text, Message.SENT_BY_ME);
                addToChat(response, Message.SENT_BY_BOT);
            }
        });
        secondlayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                String text = "강아지 알러지 요인 알려주세요.";
                String response = "강아지에게는 다양한 알러지 요인이 있습니다! \n강아지에게 식이 알러지는 눈물, 피부 가려움, 부움, 설사 등을 부작용을 동반합니다.\n가장 대표적으로 소고기와 닭고기, 양고기 등 있습니다. \n 만약 알러지 요인을 모르신다면 다양한 사료를 통해 찾으시는 것을 추천드립니다.";
                addToChat(text, Message.SENT_BY_ME);
                addToChat(response, Message.SENT_BY_BOT);
            }
        });
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}