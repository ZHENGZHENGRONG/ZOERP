package com.zhongou.view.examination.approvaldetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongou.R;
import com.zhongou.base.BaseActivity;
import com.zhongou.common.MyException;
import com.zhongou.dialog.Loading;
import com.zhongou.helper.UserHelper;
import com.zhongou.inject.ViewInject;
import com.zhongou.model.MyApprovalModel;
import com.zhongou.model.approvaldetailmodel.ProcurementApvlModel;
import com.zhongou.utils.PageUtil;

/**
 * 审批 采购详情
 * Created by sjy on 2017/1/16.
 */

public class ProcurementDetailApvlActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right)
    TextView tv_right;


    //申请人
    @ViewInject(id = R.id.tv_ApprovalPerson)
    TextView tv_ApprovalPerson;

    //部门
    @ViewInject(id = R.id.tv_approvaldept)
    TextView tv_approvaldept;

    //公司
    @ViewInject(id = R.id.tv_approvalCo)
    TextView tv_approvalCo;

    //申请时间
    @ViewInject(id = R.id.tv_approvalTime)
    TextView tv_approvalTime;


    //未审批bottom
    @ViewInject(id = R.id.laytout_decide)
    LinearLayout laytout_decide;

    //驳回
    @ViewInject(id = R.id.btn_refulse, click = "forRefulse")
    Button btn_refulse;

    //批准
    @ViewInject(id = R.id.btn_commit, click = "toForCommit")
    Button btn_commit;

    //转交
    @ViewInject(id = R.id.btn_transfer, click = "forTransfer")
    Button btn_transfer;

    //审批bottom
    @ViewInject(id = R.id.laytout_copy)
    LinearLayout laytout_copy;

    //抄送
    @ViewInject(id = R.id.btn_copytp, click = "forCopyto")
    Button btn_copytp;

    //物品名称
    @ViewInject(id = R.id.tv_procurement_thingsName)
    TextView tv_procurement_thingsName;

    //类型
    @ViewInject(id = R.id.tv_procurement_thingsType)
    TextView tv_procurement_thingsType;

    //规格
    @ViewInject(id = R.id.tv_procurement_ItemSpecifics)
    TextView tv_procurement_ItemSpecifics;

    //型号
    @ViewInject(id = R.id.tv_procurement_ItemSize)
    TextView tv_procurement_ItemVersion;

    //数量
    @ViewInject(id = R.id.tv_procurement_ItemNumber)
    TextView tv_procurement_ItemNumber;

    //金额
    @ViewInject(id = R.id.tv_procurement_ItemFees)
    TextView tv_ritv_procurement_ItemFees;

    //理由
    @ViewInject(id = R.id.tv_procurement_buyFor)
    TextView tv_procurement_buyFor;

    //购买人
    @ViewInject(id = R.id.tv_procurement_buyer)
    TextView tv_procurement_buyer;

    //计划购买时间
    @ViewInject(id = R.id.tv_procurement_PlanBuyTime)
    TextView tv_procurement_PlanBuyTime;

    //备注
    @ViewInject(id = R.id.tv_procurement_Other)
    TextView tv_procurement_Other;

    //审批人
    @ViewInject(id = R.id.tv_Requester)
    TextView tv_Requester;

    private MyApprovalModel myApprovalModel;
    private ProcurementApvlModel model;
    //常量
    public static final int POST_SUCCESS = 21;
    public static final int POST_FAILED = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apps_examination_procurement_d2);
        tv_title.setText(getResources().getString(R.string.procurement));
        tv_right.setText("");
        Intent intent = getIntent();
        myApprovalModel = (MyApprovalModel) intent.getSerializableExtra("MyApprovalModel");

        bottomType();
        //
        getDetailData();
    }

    private void setShow(ProcurementApvlModel model) {
        //
        tv_ApprovalPerson.setText(model.getEmployeeName());
        tv_approvaldept.setText(model.getDepartmentName());
        tv_approvalCo.setText(model.getStoreName());
        tv_approvalTime.setText(model.getApplicationCreateTime());

        //
        tv_procurement_thingsName.setText(model.getItemName());
        tv_procurement_thingsType.setText(model.getItemType());
        tv_procurement_ItemSpecifics.setText(model.getSpecification());
        tv_procurement_ItemVersion.setText(model.getVersions());
        tv_procurement_ItemNumber.setText(model.getAmount());
        tv_ritv_procurement_ItemFees.setText(model.getEstimateFee());
        tv_procurement_buyer.setText(model.getBuyer());
        tv_procurement_buyFor.setText(model.getReason());
        tv_procurement_PlanBuyTime.setText(model.getPlanTime());
        tv_procurement_Other.setText(model.getRemark());


        if (model.getApprovalInfoLists().size() > 0) {
            tv_Requester.setText(model.getApplicationCreateTime());
        } else {
            tv_Requester.setText("未审批");
        }
    }

    private void bottomType() {
        //
        if (myApprovalModel.getApprovalStatus().contains("1")) {

            laytout_decide.setVisibility(View.GONE);
            laytout_copy.setVisibility(View.VISIBLE);

        } else {
            laytout_decide.setVisibility(View.VISIBLE);
            laytout_copy.setVisibility(View.GONE);
        }
    }

    private void getDetailData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    ProcurementApvlModel model = UserHelper.approvalDetailPostProcurement(ProcurementDetailApvlActivity.this,
                            myApprovalModel.getApplicationID(),
                            myApprovalModel.getApplicationType());
                    sendMessage(POST_SUCCESS, model);
                } catch (MyException e) {
                    sendMessage(POST_FAILED, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case POST_SUCCESS:
                model = (ProcurementApvlModel) msg.obj;
                setShow(model);
                break;
            case POST_FAILED:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    //驳回
    public void forRefulse(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonDisagreeActivity.class, bundle);
    }

    //
    public void toForCommit(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonAgreeActivity.class, bundle);
    }

    //转交
    public void forTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonTransfertoActivity.class, bundle);
    }

    // 抄送
    public void forCopyto(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("MyApprovalModel", myApprovalModel);
        startActivity(CommonCopytoCoActivity.class, bundle);
    }

    /**
     * back
     *
     * @param view
     */
    public void forBack(View view) {
        this.finish();
    }
}
