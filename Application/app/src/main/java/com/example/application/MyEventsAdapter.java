package com.example.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.MyEventsViewHolder>{

    private JSONObject[] eventsArray;
    private MyEventsActivity currentActivity;

    //Constructor
    public MyEventsAdapter(JSONObject[] eventsList, MyEventsActivity currentActivity){
        this.currentActivity = currentActivity;
        this.eventsArray = eventsList;
    }

    @NonNull
    @Override
    public MyEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_events_card, parent, false);
        return new MyEventsViewHolder(view, currentActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventsViewHolder holder, int position) {
        // Bind your data to the card layout here
        try{


            JSONObject event = eventsArray[position];
            String eventID = (String) event.get("_id");
            holder.bindData(eventID);

            String eventName = (String)event.get("name");
            String eventDateLocation = (String)event.get("date") + " at " + (String)event.get("location");
            String eventHost = "Hosted by " + (String)event.get("host");
            String eventDescription = "There is no description for this event.";
            try{
                eventDescription = (String)event.get("description");
            } catch (Exception e) {

            }
            String[] joinedUsers;
            try{
                JSONArray joinedU = (JSONArray) event.get("joinedUsers");
                joinedUsers = new String[joinedU.length()];
                for (int i = 0; i < joinedU.length(); i++) {
                    joinedUsers[i] = joinedU.getString(i);
                }
                System.out.println(joinedUsers);

            } catch (Exception e){
                e.printStackTrace();
                joinedUsers = new String[]{};
            }
            String users = "";
            if (joinedUsers.length == 0){
                users += "There are no other attendees";
            } else {
                users += "The attendees: ";
                for (int i = 0; i < joinedUsers.length; i++){
                    users += (joinedUsers[i] + ", ");
                }
                users = users.substring(0, users.length() - 2) + ".";
            }

            MaterialCardView cv = holder.getCardView();
            LinearLayout layout1 = cv.findViewById(R.id.event_card_layout_1);

            TextView tv1 = layout1.findViewById(R.id.card_text_1);
            tv1.setText(eventName);
            TextView tv2 = layout1.findViewById(R.id.card_text_2);
            tv2.setText(eventDateLocation);
            TextView tv3 = layout1.findViewById(R.id.card_text_3);
            tv3.setText(eventHost);
            TextView tv4 = layout1.findViewById(R.id.card_text_4);
            tv4.setText(eventDescription);
            TextView tv5 = layout1.findViewById(R.id.card_text_5);
            tv5.setText(users);


        } catch(Exception e){
            MaterialCardView cvE = holder.getCardView();
            LinearLayout layout1E = cvE.findViewById(R.id.event_card_layout_1);
            TextView tv1E = layout1E.findViewById(R.id.card_text_1);
            tv1E.setText("Error in MyEventsAdapter");
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        // Return the number of items in your RecyclerView
        return eventsArray.length;
    }

    public class MyEventsViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        MaterialButton leaveButton;
        MyEventsActivity currentActivity;

        public MyEventsViewHolder(View itemView, MyEventsActivity currentActivity) {
            super(itemView);
            // define click listener for viewholder's view
            leaveButton = itemView.findViewById(R.id.card_leave_button);
            this.currentActivity = currentActivity;

            cardView = (MaterialCardView) itemView.findViewById(R.id.event_card);
        }

        public MaterialCardView getCardView(){
            return cardView;
        }

        public void bindData(final String eventID) {
            leaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click event here using data
                    System.out.println("Leave button clicked. but also, : " + eventID);
                    currentActivity.leaveEvent(eventID, v);
                }
            });
        }

    }


}
