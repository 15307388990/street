/**
 *
 */
package com.juxun.business.street.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iboxpay.print.IPrintJobStatusCallback;
import com.iboxpay.print.PrintManager;
import com.iboxpay.print.model.CharacterParams;
import com.iboxpay.print.model.PrintItemJobInfo;
import com.iboxpay.print.model.PrintJobInfo;
import com.juxun.business.street.bean.GiveOrder;
import com.juxun.business.street.util.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yl.ming.efengshe.R;

/**
 * @author WuJianHua 选择支付方式弹框
 */
public class PaySuccessActivity extends BaseActivity {
    private final static String TAG = "打印机 客户端";
    @ViewInject(R.id.type)
    private TextView s_type;// 扫码方式 0 微信 1 支付宝 2盒子
    @ViewInject(R.id.s_totalmoney)
    private TextView s_totalmoney;// 交易金额
    @ViewInject(R.id.s_ordernum)
    private TextView s_ordernum;// 交易单号
    @ViewInject(R.id.succ_time)
    private TextView succ_time;// 交易时间
    @ViewInject(R.id.pay_time)
    private TextView pay_time;// 支付时间
    @ViewInject(R.id.mPrint)
    private Button mPrint;// 打印
    @ViewInject(R.id.storname)
    private TextView storname;// 商户名称

    private GiveOrder giveOrder;
    private PrintManager mPrintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        ViewUtils.inject(this);
        giveOrder = (GiveOrder) getIntent().getExtras().getSerializable("give");
        mPrintManager = (PrintManager) this.getSystemService("iboxpay_print");
        initTitle();
        initDate();
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 单击事件
     */
    @OnClick({R.id.mPrint})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.mPrint:
                if (giveOrder != null) {
                    mPrintManager.printLocaleJob(createPrintReceiptTask222(), mTaskCallback);
                }
                break;
        }
    }

    private IPrintJobStatusCallback mTaskCallback = new IPrintJobStatusCallback.Stub() {
        @Override
        public void onPrintJobStatusChange(int status, String taskId) throws RemoteException {
            Log.i(TAG, "onPrintTaskStatusChange status = " + status + "taskId=" + taskId);
        }

    };

    @Override
    public void onResponse(String response, String url) {

    }

    /* 加载数据 */
    private void initDate() {
        if (giveOrder != null) {
            switch (giveOrder.getOrder_pay_type()) {    //1支付宝支付、2微信、3盒子支付
                case 1:
                    s_type.setText("支付宝支付");
                    break;
                case 2:
                    s_type.setText("微信支付");
                    break;
                case 3:
                    s_type.setText("盒子支付");
                    break;
                default:
                    break;
            }
            s_totalmoney.setText(mDf.format(Tools.getFenYuan(giveOrder.getOrder_price())));
            s_ordernum.setText(giveOrder.getOrder_id());
            try {
                //succ_time 创建的时间,pay_time支付完成时间
                succ_time.setText(Tools.getDateformat2(giveOrder.getCreate_date()));
                pay_time.setText(Tools.getDateformat2(giveOrder.getOrder_end_date()));
            } catch (Exception e) {
            }
            storname.setText(partnerBean.getStore_name());
        }
        if (mSavePreferencesData.getBooleanData("isSn")) {
            mPrint.setVisibility(View.VISIBLE);
        } else {
            mPrint.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化标题栏
     */
    public void initTitle() {
        titleBar = findViewById(R.id.titleBar);
        back = findViewById(R.id.top_view_back);
        title = (TextView) findViewById(R.id.top_view_text);
        more = (TextView) findViewById(R.id.right_view_text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.stop();
                finish();
            }
        });
    }

    // add by zhengzhenxing
    private PrintJobInfo createPrintReceiptTask222() {
        PrintJobInfo receiptTask = new PrintJobInfo();
        try {
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo("   支付清单      \n", new CharacterParams(2, 2)));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  商户名称:" + partnerBean.getStore_name() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  订单单号:" + giveOrder.getOrder_id() + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  创建时间:" + Tools.getDateformat2(giveOrder.getCreate_date()) + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  交易时间:" + Tools.getDateformat2(giveOrder.getOrder_end_date()) + "\n\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("------------------------------\n", new CharacterParams()));
            receiptTask.addPrintItemJobTask(
                    new PrintItemJobInfo("  RMB:" + mDf.format(Tools.getFenYuan(giveOrder.getOrder_price())) + "\n\n", new CharacterParams(2, 2)));
            receiptTask.addPrintItemJobTask(new PrintItemJobInfo(
                    "         支付方式:" + s_type.getText().toString() + "\n\n\n\n\n\n", new CharacterParams()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiptTask;
    }
}