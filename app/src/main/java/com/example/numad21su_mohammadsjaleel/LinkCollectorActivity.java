package com.example.numad21su_mohammadsjaleel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class LinkCollectorActivity extends AppCompatActivity {
    //Creating the essential parts needed for a Recycler view to work: RecyclerView, Adapter, LayoutManager
    private ArrayList<ItemCard> itemList = new ArrayList<>();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText popup_name;
    private EditText popup_url;
    private Button popup_cancel;
    private Button popup_save;
    ConstraintLayout constraintLayout;

    private RecyclerView recyclerView;
    private RviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private FloatingActionButton addButton;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private static final String HTTPS = "https://";
    private static final String HTTP = "http://";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);

        init(savedInstanceState);

        addButton = findViewById(R.id.addButton);
        constraintLayout = findViewById(R.id.link_collector_layout);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int pos = 0;
                createUrlPopup();
//                addItem(pos);
            }
        });

        //Specify what action a specific gesture performs, in this case swiping right or left deletes the entry
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(LinkCollectorActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getLayoutPosition();
                itemList.remove(position);

                rviewAdapter.notifyItemRemoved(position);

            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    // Handling Orientation Changes on Android
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // This is only a possible way to do, please find your own way to generate the key
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getImageSource());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getUrlName());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getUrlText());
        }
        super.onSaveInstanceState(outState);

    }

    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");
                    boolean isChecked = savedInstanceState.getBoolean(KEY_OF_INSTANCE + i + "3");

                    // We need to make sure names such as "XXX(checked)" will not duplicate
                    // Use a tricky way to solve this problem, not the best though
                    if (isChecked) {
                        itemName = itemName.substring(0, itemName.lastIndexOf("("));
                    }
                    ItemCard itemCard = new ItemCard(imgId, itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }
        // The first time to opne this Activity
        else {
//            ItemCard item1 = new ItemCard(R.drawable.pic_gmail_01, "Gmail", "Example description", false);
//            ItemCard item2 = new ItemCard(R.drawable.pic_google_01, "Google", "Example description", false);
//            ItemCard item3 = new ItemCard(R.drawable.pic_youtube_01, "Youtube", "Example description", false);
//            itemList.add(item1);
//            itemList.add(item2);
//            itemList.add(item3);
        }

    }

    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rviewAdapter = new RviewAdapter(itemList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //attributions bond to the item has been changed
                itemList.get(position).onItemClick(position);

                rviewAdapter.notifyItemChanged(position);
            }
        };
        rviewAdapter.setOnItemClickListener(itemClickListener);

        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }

    private boolean addItem(int position) {
        String potentialUrl = popup_url.getText().toString();
        if (!Patterns.WEB_URL.matcher(potentialUrl).matches()) {
            Toast.makeText(LinkCollectorActivity.this, "Invalid Url", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!potentialUrl.startsWith(HTTP) && !potentialUrl.startsWith(HTTPS)) {
            potentialUrl = HTTP + potentialUrl;
        }
        itemList.add(position, new ItemCard(R.drawable.empty,
                popup_name.getText().toString(), potentialUrl));
//        Toast.makeText(LinkCollectorActivity.this, "URL added successfully!", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "URL Created!", Snackbar.LENGTH_LONG);
        snackbar.show();

        rviewAdapter.notifyItemInserted(position);
        return true;
    }

    public void createUrlPopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View urlPopupView = getLayoutInflater().inflate(R.layout.url_popup, null);
        popup_name = urlPopupView.findViewById(R.id.url_popup_name);
        popup_url = urlPopupView.findViewById(R.id.url_popup_url);
        popup_save = urlPopupView.findViewById(R.id.url_popup_save);
        popup_cancel = urlPopupView.findViewById(R.id.url_popup_cancel);
        dialogBuilder.setView(urlPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        popup_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addItem(0)) {
                    dialog.dismiss();
                }
            }
        });
        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}