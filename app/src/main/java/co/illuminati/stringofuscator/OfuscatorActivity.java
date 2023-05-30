package co.illuminati.stringofuscator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class OfuscatorActivity extends AppCompatActivity {

    EditText key;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofuscator);
        key = findViewById(R.id.string_text);
        /*key.setOnEditorActionListener((v, ationId, event) -> {
            if (ationId == IME_ACTION_DONE) {
                if (findViewById(R.id.ofuscate).isEnabled()) {
                    findViewById(R.id.ofuscate).performClick();
                }
                return true;
            }
            return false;
        });*/
        key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                if (text.trim().isEmpty()) {
                    findViewById(R.id.ofuscate).setEnabled(false);
                } else {
                    findViewById(R.id.ofuscate).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        findViewById(R.id.ofuscate).setOnClickListener(view -> {
            result = new ofuscate().getStr(key.getText().toString());
            MaterialAlertDialogBuilder d = new MaterialAlertDialogBuilder(this, R.style.Theme_MaterialComponents_DayNight_Dialog)
                    .setTitle(R.string.title)
                    .setMessage(result)
                    .setPositiveButton(R.string.postive_btn, (dialogInterface, i) -> {
                        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("string", result));
                        Snackbar.make(findViewById(R.id.ofuscate), R.string.copied, BaseTransientBottomBar.LENGTH_LONG).show();
                    });
            d.show();
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.menu), R.style.Theme_MaterialComponents_DayNight);
                Menu menu = popupMenu.getMenu();
                menu.add(R.string.illuminate);
                menu.add(R.string.codeX);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getTitle().toString()) {
                        case "Team illuminate":
                            openLink(getString(R.string.team_illuminate));
                            break;
                        case "CodeX Cheats":
                            openLink(getString(R.string.codex_cheats));
                            break;
                    }
                    return false;
                });
                popupMenu.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ofuscate {
        public String getStr(String str) {
            Random random = new Random(System.currentTimeMillis());
            byte[] bytes = str.getBytes();
            int lenght = bytes.length;
            StringBuilder append = new StringBuilder().append("(new Object() {").append("\n int illuminati;").append("\n public String toString() {").append("\n      byte[] MrCode_403 = new byte[").append(lenght).append("];");
            for (int i = 0; i < lenght; i++) {
                int nextInt = random.nextInt(24) + 1;
                append = append.append("\n       illuminati = ").append((random.nextInt() & ((255 << nextInt) ^ -1)) | (bytes[i] << nextInt)).append(";").append("\n MrCode_403[").append(i).append("] = (byte) (illuminati >>>").append(nextInt).append(");");
            }
            result = append.append("\n return new String(MrCode_403);").append("\n  }\n}.toString())").toString();
            return result;
        }
    }

    private void openLink(String link) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }


}