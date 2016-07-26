package digitransport.android.com.digitransport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.util.zip.InflaterInputStream;

import net.sourceforge.zbar.Symbol;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.android.digitransport.R;

public class ScanCode extends AppCompatActivity{
    TextView rcRegNo,regDate,chechisno,mnfr,osNo,engNo,color,vhlType,userName,fName,address,model,bodyType,cylNo,baseNo,mfgDate,
             unladen,fuleType,seating,regUpto,standC,taxupto,cucap;
    TextView topLavel,lavel2,pn,inv,bju,issueDate,lNo,lName,swd,lAddress,validity,carrno;
	
	
    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ZBarScannerActivity.class);
        intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
        startActivityForResult(intent, 1);
    }
    
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data)
    {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep09);
        try {
            if (resultCode == RESULT_OK) {
               // mp.start();
                String result = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                EncDec endec=new EncDec(ScanCode.this);
                PublicKey publicKey = endec.readPublicKey();
                byte[] decrypt=endec.decryptWithPublic(publicKey, result.getBytes());
                String res=decompress(decrypt);
                String  res1=res.replaceAll("\"", "");
                String[] reseultArray=res1.split(",");
                if(reseultArray[0].contains("VAHAN")) {
                    setContentView(R.layout.scan_rc2);
                    rcRegNo=(TextView)findViewById(R.id.lavel_top);
                    regDate=(TextView)findViewById(R.id.regdt);
                    chechisno=(TextView)findViewById(R.id.chid);
                    mnfr=(TextView)findViewById(R.id.mfrid);
                    osNo=(TextView)findViewById(R.id.snoid);
                    engNo=(TextView)findViewById(R.id.enoid);
                    color=(TextView)findViewById(R.id.colid);
                    vhlType=(TextView)findViewById(R.id.vhid);
                    userName=(TextView)findViewById(R.id.namerc);
                    fName=(TextView)findViewById(R.id.swdid);
                    address=(TextView)findViewById(R.id.addrcid);
                    model=(TextView)findViewById(R.id.modid);
                    bodyType=(TextView)findViewById(R.id.bodid);
                    cylNo=(TextView)findViewById(R.id.cylid);
                    baseNo=(TextView)findViewById(R.id.baseid);
                    mfgDate=(TextView)findViewById(R.id.mfgrcid);
                    unladen=(TextView)findViewById(R.id.unledid);
                    fuleType=(TextView)findViewById(R.id.fuelid);
                    seating=(TextView)findViewById(R.id.seatid);
                    regUpto=(TextView)findViewById(R.id.regid);
                    standC=(TextView)findViewById(R.id.standid);
                    taxupto=(TextView)findViewById(R.id.taxid);
                    cucap=(TextView)findViewById(R.id.cupid);

                    rcRegNo.setText("REG NO: "+reseultArray[2]);
                    userName.setText("NAME : "+reseultArray[3]);
                   // String fTest[]=reseultArray[4].split(" ", 2);
                    fName.setText("S/W/D of : "+reseultArray[4]);
                    address.setText(reseultArray[5]+","+reseultArray[6]);
                    vhlType.setText("VEH CL : "+reseultArray[7]);
                    chechisno.setText("CH NO : "+reseultArray[8]);
                    engNo.setText("E NO : "+reseultArray[9]);
                    mnfr.setText("MFR : "+reseultArray[10]);
                    model.setText(reseultArray[11]);
                    fuleType.setText(reseultArray[12]);
                    color.setText("COLOR : "+reseultArray[13]);
                    regUpto.setText(reseultArray[14]);
                    String mfgdate=reseultArray[16].replace("\\","");
                    mfgDate.setText(mfgdate);
                    unladen.setText(reseultArray[17]);
                    cylNo.setText(reseultArray[18]);
                    cucap.setText(reseultArray[19]);
                    seating.setText(reseultArray[20]);
                   
                }else if(reseultArray[0].contains("[SARTHI")){
                    setContentView(R.layout.scan_dl);
                    topLavel=(TextView)findViewById(R.id.lavel_top);
                    lavel2=(TextView)findViewById(R.id.lavel_top1);
                    pn=(TextView)findViewById(R.id.yn);
                    inv=(TextView)findViewById(R.id.dl_authid);
                    bju=(TextView)findViewById(R.id.ynid);
                    issueDate=(TextView)findViewById(R.id.doid);
                    lNo=(TextView)findViewById(R.id.lnoid);
                    lName=(TextView)findViewById(R.id.nameid);
                    swd=(TextView)findViewById(R.id.swid);
                    lAddress=(TextView)findViewById(R.id.addid);
                    validity=(TextView)findViewById(R.id.val_id);
                    carrno=(TextView)findViewById(R.id.naid);

                    issueDate.setText(reseultArray[6]);
                    lNo.setText(reseultArray[1]);
                    lName.setText(reseultArray[2]);
                    swd.setText(reseultArray[3]);
                    address.setText(reseultArray[4]);
                    validity.setText(reseultArray[5]);

                }else{
                    Toast.makeText(this,"Invalid QRCode!!! Try Again.",Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Invalid QRCode!!! Try Again.",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,ActivityMain.class);
            startActivity(intent);
        }
    }
 
    public static String decompress(byte[] bytes) {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[2048];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            String str=new String(baos.toByteArray(), "UTF-8");
            return str;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

}
