package ir.iamnovinfar.Shorten_link.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andreseko.SweetAlert.SweetAlertDialog;

import org.w3c.dom.Text;

import java.util.List;

import ir.iamnovinfar.Shorten_link.Activity.MainActivity;
import ir.iamnovinfar.Shorten_link.Model.GsonModel.GetLinks.GetLinksGsonModel;
import ir.iamnovinfar.Shorten_link.R;


public class RecyclerViewGenaratesLink extends RecyclerView.Adapter<RecyclerViewGenaratesLink.myViewHolder> {

    Context context;
    GetLinksGsonModel getLinksGsonModel;
    myViewHolder itemViewHolder;
    SweetAlertDialog sweetAlertDialog;

    public RecyclerViewGenaratesLink(Context context, GetLinksGsonModel getLinksGsonModel) {
        this.context = context;
        this.getLinksGsonModel = getLinksGsonModel;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_genaratedlinks,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        itemViewHolder = (myViewHolder) holder;

        itemViewHolder.short_link.setText("https://lnkno.ir/"+getLinksGsonModel.getUserLinks().get(position).getShortUrl());
        itemViewHolder.clickCount.setText("تعداد کلیک شده: "+getLinksGsonModel.getUserLinks().get(position).getViewCount().toString());
        itemViewHolder.orginal_link.setText(getLinksGsonModel.getUserLinks().get(position).getOriginalUrl());

        if (getLinksGsonModel.getUserLinks().get(position).getApproved()==1){
            itemViewHolder.link_status.setImageResource(R.drawable.ic_baseline_check_24);
            itemViewHolder.short_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://lnkno.ir/"+getLinksGsonModel.getUserLinks().get(position).getShortUrl()); // missing 'http://' will cause crashed
                    Intent intent3 = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent3);
                }
            });



        }else if (getLinksGsonModel.getUserLinks().get(position).getApproved()==0){
            itemViewHolder.link_status.setImageResource(R.drawable.ic_baseline_clear_24);
            itemViewHolder.short_link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitle("وضعیت لینک مسدود است لطفا با پشتیبانی تماس بگیرید");
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return getLinksGsonModel.getUserLinks().size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView short_link,clickCount,orginal_link;
        ImageView link_status;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            short_link=itemView.findViewById(R.id.short_links_archive);
            clickCount=itemView.findViewById(R.id.clickCounttxt);
            link_status=itemView.findViewById(R.id.link_status);
            orginal_link=itemView.findViewById(R.id.orginal_link);


        }
    }
}
