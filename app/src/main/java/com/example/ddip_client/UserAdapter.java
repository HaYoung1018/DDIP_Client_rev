package com.example.ddip_client;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private Context context; // Context를 통해 SharedPreferences 접근

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context; // Context 저장
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameTextView.setText(user.getName());
        holder.phoneTextView.setText(user.getPhoneNumber());
        holder.profileImageView.setImageResource(user.getProfileImageResId());

        // SharedPreferences에서 userType 가져오기
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("userType", "");

        // userType에 따라 급여와 근무시간 표시/숨기기
        if ("Staff".equals(userType)) {
            holder.salaryAndHoursTextView.setVisibility(View.GONE); // Staff는 숨기기
        } else {
            String salaryAndHoursText = "급여: " + user.getSalary() + "원 | 근무시간: " + user.getWorkHours() + "시간";
            holder.salaryAndHoursTextView.setText(salaryAndHoursText);
            holder.salaryAndHoursTextView.setVisibility(View.VISIBLE); // Owner는 보이기
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addUser(User user) {
        userList.add(user);
        notifyItemInserted(userList.size() - 1);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView,salaryAndHoursTextView ;
        ImageView profileImageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
            profileImageView = itemView.findViewById(R.id.profile_image_view);
            salaryAndHoursTextView = itemView.findViewById(R.id.salary_and_hours_text_view); // 연결
        }
    }
}
