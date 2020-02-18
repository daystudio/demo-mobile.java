package mou.demo_java;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.steamcrafted.materialiconlib.MaterialMenuInflater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mou.demo_java.model.P;
import mou.util.Util;

public class MainActivity extends AppCompatActivity {
    private final static String SZ_URL = "https://crt8b3p0n9.execute-api.ap-southeast-1.amazonaws.com/prod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rv_movie = findViewById(R.id.rv_movie);
        rv_movie.setLayoutManager(new LinearLayoutManager(this));

        Util.http_get(SZ_URL, new Util.Callback_HTTP() {
            @Override
            public void on_callback(String sz_json) {
                // parsing data back
                final ArrayList<P> ret = new ArrayList<P>();
                try {
                    JSONArray ary_json = new JSONArray(sz_json);
                    for (int i = 0; i < ary_json.length(); i++) {
                        JSONObject json = ary_json.getJSONObject(i);
                        ret.add(new P(json.getInt("id"), //
                                json.getString("c"), //
                                json.getString("t"), //
                                json.getString("u"), //
                                json.getString("i"), //
                                json.getString("a")//
                        ));
                    }
                } catch (Exception e) {
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv_movie.setAdapter(new Adapter_A(ret));
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MaterialMenuInflater.with(this).setDefaultColor(Color.WHITE).inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                CoordinatorLayout view = (CoordinatorLayout) li.inflate(R.layout.bottom_sheet, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(this);

                final Button btn_post = view.findViewById(R.id.btn_post);
                final EditText et_c = view.findViewById(R.id.et_c);
                final EditText et_t = view.findViewById(R.id.et_t);
                final EditText et_u = view.findViewById(R.id.et_u);
                final EditText et_i = view.findViewById(R.id.et_i);
                final EditText et_a = view.findViewById(R.id.et_a);
                final RecyclerView rv_movie = findViewById(R.id.rv_movie);

                btn_post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject json = new JSONObject() {{
                                put("c", et_c.getText());
                                put("t", et_t.getText());
                                put("u", et_u.getText());
                                put("i", et_i.getText());
                                put("a", et_a.getText());
                            }};
                            Util.http_post(SZ_URL, json.toString(), new Util.Callback_HTTP() {
                                @Override
                                public void on_callback(String sz_json) {
                                    // parsing data back
                                    final ArrayList<P> ret = new ArrayList<P>();
                                    try {
                                        JSONArray ary_json = new JSONArray(sz_json);
                                        for (int i = 0; i < ary_json.length(); i++) {
                                            JSONObject json = ary_json.getJSONObject(i);
                                            ret.add(new P(json.getInt("id"), //
                                                    json.getString("c"), //
                                                    json.getString("t"), //
                                                    json.getString("u"), //
                                                    json.getString("i"), //
                                                    json.getString("a")//
                                            ));
                                        }
                                    } catch (Exception e) {
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rv_movie.setAdapter(new Adapter_A(ret));
                                            dialog.hide();
                                            Toast.makeText(getApplicationContext(), "Posted.", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                });
                dialog.setContentView(view);
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //------------------------------------------------------
    //  RecyclerView UI Classes BEGAN
    class Adapter_A extends RecyclerView.Adapter<Adapter_A.ViewHolder_A> {
        private ArrayList<P> ary_items;

        public Adapter_A(ArrayList<P> items) {
            ary_items = items;
        }

        @Override
        public Adapter_A.ViewHolder_A onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder_A(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder_A holder, int position) {
            holder.tv_c.setText(ary_items.get(position).getC());
            holder.tv_t.setText(ary_items.get(position).getT());
            holder.tv_u.setText(ary_items.get(position).getU());
            Picasso.get().load(ary_items.get(position).getI()).into(holder.iv_i);
            Picasso.get().load(ary_items.get(position).getA()).into(holder.iv_a);
        }

        @Override
        public int getItemCount() {
            return ary_items.size();
        }

        class ViewHolder_A extends RecyclerView.ViewHolder {
            private TextView tv_c;
            private TextView tv_t;
            private TextView tv_u;
            private ImageView iv_i;
            private ImageView iv_a;

            public ViewHolder_A(View v) {
                super(v);
                tv_c = v.findViewById(R.id.tv_c);
                tv_t = v.findViewById(R.id.tv_t);
                tv_u = v.findViewById(R.id.tv_u);
                iv_i = v.findViewById(R.id.iv_i);
                iv_a = v.findViewById(R.id.iv_a);
            }
        }
    }
    //  RecyclerView UI Classes ENDED
    //------------------------------------------------------
}
