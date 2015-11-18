package com.pandora.rsvp.ui.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Field label for CreateInvitationActivity.  e.g., "Invitation Description"
 */
public class FieldLabel extends TextView {
    public FieldLabel(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Nimbus Sans L CY Bold.ttf"));
    }
}
