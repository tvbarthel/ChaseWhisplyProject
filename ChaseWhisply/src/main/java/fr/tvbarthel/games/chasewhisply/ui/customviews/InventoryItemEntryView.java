package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.ui.InventoryCraftListener;

public class InventoryItemEntryView extends RelativeLayout {

    private Context mContext;
    private InventoryItemEntry mModel;
    private TextView mTitle;
    private TextView mQuantity;
    private ImageButton mCraftButton;
    private ImageView mItemImage;

    public InventoryItemEntryView(Context context) {
        this(context, null);
    }

    public InventoryItemEntryView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;

        final int halfPadding = context.getResources().getDimensionPixelSize(R.dimen.half_padding);
        setBackgroundResource(R.drawable.card_shadow);
        setPadding(halfPadding, halfPadding, halfPadding, halfPadding);
        setClickable(true);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_inventory_item_entry, this, true);

        mTitle = (TextView) findViewById(R.id.view_inventory_item_entry_title);
        mQuantity = (TextView) findViewById(R.id.view_inventory_item_entry_quantity);
        mCraftButton = (ImageButton) findViewById(R.id.view_inventory_item_entry_craft_action);
        mItemImage = (ImageView) findViewById(R.id.view_inventory_item_entry_item_image);
    }

    public void setModel(final InventoryItemEntry model) {
        mModel = model;
        final long quantityAvailable = mModel.getQuantityAvailable();
        final int titleResourceId = mModel.getTitleResourceId();
        final int imageResourceId = mModel.getImageResourceId();
        mTitle.setText(mContext.getResources().getQuantityText(titleResourceId, 1));
        mQuantity.setText("x" + String.valueOf(quantityAvailable));
        mItemImage.setImageResource(imageResourceId);
        if (mModel.getRecipe().getIngredientsAndQuantities().size() == 0) {
            mCraftButton.setEnabled(false);
        }
    }

    public void setCraftEnable(boolean isEnable) {
        mCraftButton.setEnabled(isEnable);
    }

    public void setDetailRequestListener(final Listener listener) {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInventoryItemEntryDetailRequest(mModel);
            }
        });
    }

    public void setCraftRequestListener(final InventoryCraftListener listener) {
        mCraftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCraftRequested(mModel);
            }
        });
    }

    public interface Listener {
        public void onInventoryItemEntryDetailRequest(InventoryItemEntry inventoryItemEntry);
    }
}
