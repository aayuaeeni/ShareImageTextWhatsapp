package bugsolvers.bms.com.reactiveandroidtut.wtsApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.io.File;

import bugsolvers.bms.com.reactiveandroidtut.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;


public class WhatsappActivity extends AppCompatActivity
{
    private ImageView ivProfileImage, ivEditProfile;
    private EditText etStatus;
    private Button btnDirect;
    private  Uri uri;

    private String imagePath, selectedImagePath;
    public static String formattedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        ivEditProfile = findViewById(R.id.ivEditProfile);
        etStatus = findViewById(R.id.etStatus);
        btnDirect = findViewById(R.id.btnDirect);

        ivEditProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                openChooser();
            }
        });

        btnDirect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //whatsapp(WhatsappActivity.this,"+91 8670389907");
                shareOnWhatsapp(WhatsappActivity.this,etStatus.getText().toString().trim(),uri);

            }
        });
    }

    private void openChooser()
    {
        FilePickerBuilder.getInstance().setMaxCount(1).enableCameraSupport(true).pickPhoto(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:

                if (resultCode == Activity.RESULT_OK && data != null)
                {

                    if ((data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)) != null && (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)).size() > 0)
                    {
                        selectedImagePath = (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)).get(0);

                        Glide.with(this).load(new File(selectedImagePath)).into(ivProfileImage);
                         uri = Uri.fromFile(new File(selectedImagePath));

                    }
                }
                break;
        }
    }

    public static void shareOnWhatsapp(AppCompatActivity appCompatActivity, String textBody, Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT,! TextUtils.isEmpty(textBody) ? textBody : "");

        if (fileUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
        }

        try {
            appCompatActivity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
            Toast.makeText(appCompatActivity, "No Whatsapp installed", Toast.LENGTH_SHORT).show();
        }
    }

// Direct open whatsapp in android with specific number

    @SuppressLint("NewApi")
    public static void whatsapp(Activity activity, String phone) {
       if (isValidMobile(phone))
       {
           formattedNumber = phone;
       }
        try{
            Intent sendIntent =new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT,"");
            //sendIntent.putExtra("jid", formattedNumber +"@s.whatsapp.net");
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(formattedNumber)+"@s.whatsapp.net");

            sendIntent.setPackage("com.whatsapp");
            activity.startActivity(sendIntent);
        }
        catch(Exception e)
        {
            Toast.makeText(activity,"Error/n"+ e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}
