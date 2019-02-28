package kr.co.teada.ex60rssfeed;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    ArrayList<Item> items;
    Context context;

    //생성자 alt insert
    public MyAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        //3.
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.recycler_item,viewGroup,false);
        VH holder=new VH(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        VH holder=(VH)viewHolder; //다운캐스팅

        //4. 현재 번 째(position) 데이터 연결 위해 아이템 얻어오기
        Item item=items.get(position);

        //각각의 뷰들에 값 설정-- 기차 각 칸마다
        holder.tvTitile.setText(item.getTitle());
        holder.tvDesc.setText(item.getDesc());
        holder.tvDate.setText(item.getDate());

        //이미지 세팅
        if(item.getImage() ==null){ //이미지가 없는 경우도 있어서
            holder.iv.setVisibility(View.GONE);
        }else{
            holder.iv.setVisibility(View.VISIBLE);

            //인터넷에 있는 이미지를 연결하여면 또 stream 필요
            //그렇다는 것은 별도의 Thread 써야 해
            // 못하겠다~~~ 라이브러리 쓰자!!!!!!!!!! Glide_bumptech 라이브러리!!
            //라이브러리 사용! 스레드 다 필요없어~ 만약 용량이 커서 out of memory 도 얘가 알아서 해줘 에러가 안나. Gif 도 가능
            Glide.with(context).load(item.getImage()).into(holder.iv);
        }

    }

    @Override
    public int getItemCount() {
        //1. 이거 먼저 해놔 리턴
        return items.size();
    }

    //2. 이너클래스 inner class
    class VH extends RecyclerView.ViewHolder{

        //2-1. 아이템 하나의 뷰들의 참조변수들
        TextView tvTitile;
        ImageView iv;
        TextView tvDesc;
        TextView tvDate;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvTitile=itemView.findViewById(R.id.tv_title);
            tvDesc=itemView.findViewById(R.id.tv_desc);
            tvDate=itemView.findViewById(R.id.tv_date);
            iv=itemView.findViewById(R.id.iv);

            //아이템(항목) 클릭 반응하기 리스너 추가
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String link=items.get(getLayoutPosition()).getLink();

                    //새로운 액티비티에 기사 보여주기
                    Intent intent=new Intent(context, ItemActivity.class);
                    intent.putExtra("Link", link);
                    context.startActivity(intent);

                }
            });

        }
    }

}
