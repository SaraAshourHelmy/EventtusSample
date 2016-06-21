package sara.com.Utility;

import android.view.View;

import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;
import sara.com.eventtussample.R;

/**
 * Created by sara on 6/21/2016.
 */
public class StickyAnimator extends HeaderStikkyAnimator {
    @Override
    public AnimatorBuilder getAnimatorBuilder() {
        View mHeader_image = getHeader().findViewById(R.id.header_image);
        return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
    }
}