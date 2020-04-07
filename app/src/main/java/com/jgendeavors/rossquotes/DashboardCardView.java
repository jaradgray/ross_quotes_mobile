package com.jgendeavors.rossquotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class DashboardCardView extends CardView {

    /**
     * Enum for alert mode.
     * Make sure order here matches that defined in attrs.xml
     */
    public enum AlertMode {
        NONE,
        CONFIRM,
        WARN;

        /**
         * Returns the AlertMode corresponding to the given int, defaulting to NONE.
         * We need this to convert values defined in XML (passed as int) to AlertMode values.
         *
         * @param i
         * @return
         */
        public static AlertMode fromInt(int i) {
            return  i == 1 ? CONFIRM :
                    i == 2 ? WARN :
                    NONE;
        }
    }


    // Instance variables
    private ImageView mIvIconMain;
    private TextView mTvTitle;
    private TextView mTvDetails;
    private View mAlertContainer;
    private ImageView mIvIconAlert;
    private TextView mTvAlert;


    // Constructor
    /**
     * The constructor needed for XML-based instantiation.
     *
     * @param context
     * @param attrs
     */
    public DashboardCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        // Get values from attributes
        int icMainResId;
        String titleText;
        String detailsText;
        AlertMode alertMode;
        String alertText;

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DashboardCardView,
                0, 0
        );

        try {
            icMainResId = a.getResourceId(R.styleable.DashboardCardView_icMain, -1);
            titleText = a.getString(R.styleable.DashboardCardView_titleText);
            detailsText = a.getString(R.styleable.DashboardCardView_detailsText);
            alertMode = AlertMode.fromInt(a.getInt(R.styleable.DashboardCardView_alertMode, 0));
            alertText = a.getString(R.styleable.DashboardCardView_alertText);
        } finally {
            a.recycle();
        }

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.view_dashboard_card, this);

        // Get references to widgets
        mIvIconMain = v.findViewById(R.id.view_dashboard_card_iv_icon_main);
        mTvTitle = v.findViewById(R.id.view_dashboard_card_tv_title);
        mTvDetails = v.findViewById(R.id.view_dashboard_card_tv_details);
        mAlertContainer = v.findViewById(R.id.view_dashboard_card_alert_container);
        mIvIconAlert = v.findViewById(R.id.view_dashboard_card_iv_icon_alert);
        mTvAlert = v.findViewById(R.id.view_dashboard_card_tv_alert);

        // TODO set widget data based on attributes
        mIvIconMain.setImageResource(icMainResId);
        mTvTitle.setText(titleText);
        mTvDetails.setText(detailsText);
        setAlertMode(alertMode, alertText);
    }


    // API methods

    public void setAlertMode(AlertMode alertMode, String alertText) {
        // Perform AlertMode-specific logic
        switch (alertMode) {
            case NONE:
                mAlertContainer.setVisibility(View.GONE);
                return;

            case CONFIRM:
                mIvIconAlert.setImageResource(R.drawable.ic_alert_confirm);
                // TODO set color of alert iv and tv
                break;

            case WARN:
                mIvIconAlert.setImageResource(R.drawable.ic_alert_warn);
                // TODO set color of alert iv and tv
                break;
        }

        // Perform general logic
        mTvAlert.setText(alertText);
        mAlertContainer.setVisibility(View.VISIBLE);

        // remember to call invalidate() and requestLayout() after any changes that could alter the View's appearance
        invalidate();
        requestLayout();
    }
}
