package com.pandora.rsvp.ui.components;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Section Header for CreateInvitationActivity.  e.g., "INVITATION DETAILS"
 * This font / TextView is also used on the light blue buttons.
 */
public class SectionHeader extends TextView {
    public SectionHeader(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/Nimbus Sans L CY Regular.ttf"));
    }
}
