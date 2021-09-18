package huji.postpc.y2021.tal.yichye.thebubble.Connections;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huji.postpc.y2021.tal.yichye.thebubble.R;

public class ContactHolder extends RecyclerView.ViewHolder {

    ImageView userIcon;
    TextView userName;
    TextView lastMsg;
    TextView lastMsgTime;
    TextView lastMsgDate;
    ImageView deleteIcon;
    LinearLayout nameAndMsgLayout;



    public ContactHolder(@NonNull View itemView) {
        super(itemView);
        userIcon = itemView.findViewById(R.id.userContactIcon);
        userName = itemView.findViewById(R.id.userNameContact);
        lastMsg = itemView.findViewById(R.id.lastMessageText);
        lastMsgTime = itemView.findViewById(R.id.timeLastMessageText);
        deleteIcon = itemView.findViewById(R.id.deleteChat);
        nameAndMsgLayout = itemView.findViewById(R.id.layoutForClick);
        lastMsgDate = itemView.findViewById(R.id.dateLastMessageText);
    }


    public ImageView getUserIcon() {
        return userIcon;
    }

    public TextView getLastMsg() {
        return lastMsg;
    }

    public TextView getLastMsgTime() {
        return lastMsgTime;
    }

    public TextView getUserName() {
        return userName;
    }

    public ImageView getDeleteIcon(){return deleteIcon;}

    public LinearLayout getNameAndMsgLayout() {
        return nameAndMsgLayout;
    }

    public TextView getLastMsgDate() {
        return lastMsgDate;
    }
}
