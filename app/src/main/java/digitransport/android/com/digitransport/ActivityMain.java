package digitransport.android.com.digitransport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.digitransport.R;


public class ActivityMain extends AppCompatActivity{
	Button scan;
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		scan=(Button)findViewById(R.id.scan);
		
		scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ActivityMain.this, ScanCode.class);
                startActivity(intent);
			}
		});
	}

}
