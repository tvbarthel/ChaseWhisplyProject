package fr.tvbarthel.games.chasewhisply.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

import fr.tvbarthel.games.chasewhisply.R;
import fr.tvbarthel.games.chasewhisply.model.inventory.InventoryItemEntry;
import fr.tvbarthel.games.chasewhisply.ui.InventoryCraftListener;

public class InventoryItemEntryDetailView extends ScrollView {

    private Context mContext;
    private InventoryItemEntry mModel;
    private TextView mTitle;
    private TextView mQuantity;
    private ImageButton mCraftButton;
    private TextView mDescription;
    private TextView mDroppedByLabel;
    private TextView mDroppedBy;
    private TextView mRecipe;
    private ImageView mItemImage;

    public InventoryItemEntryDetailView(Context context) {
        this(context, null);
    }

    public InventoryItemEntryDetailView(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;

        final int halfPadding = context.getResources().getDimensionPixelSize(R.dimen.half_padding);
        setPadding(halfPadding, halfPadding, halfPadding, halfPadding);

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_inventory_item_entry_details, this, true);

        mTitle = (TextView) findViewById(R.id.view_inventory_item_entry_title);
        mQuantity = (TextView) findViewById(R.id.view_inventory_item_entry_quantity);
        mCraftButton = (ImageButton) findViewById(R.id.view_inventory_item_entry_craft_action);
        mDescription = (TextView) findViewById(R.id.view_inventory_item_entry_description);
        mDroppedByLabel = (TextView) findViewById(R.id.view_inventory_item_entry_dropped_by_label);
        mDroppedBy = (TextView) findViewById(R.id.view_inventory_item_entry_dropped_by);
        mRecipe = (TextView) findViewById(R.id.view_inventory_item_entry_recipe);
        mItemImage = (ImageView) findViewById(R.id.view_inventory_item_entry_details_item_image);
    }

    public void setModel(final InventoryItemEntry model) {
        mModel = model;
        final long quantityAvailable = mModel.getQuantityAvailable();
        final int descriptionResourceId = mModel.getDescriptionResourceId();
        final int titleResourceId = mModel.getTitleResourceId();
        final int itemImageResouceId = mModel.getImageResourceId();
        mTitle.setText(mContext.getResources().getQuantityText(titleResourceId, 1));
        mDescription.setText(descriptionResourceId);
        mQuantity.setText(String.valueOf(quantityAvailable));
        mRecipe.setText(mModel.getRecipe().toString(mContext));
        mItemImage.setImageResource(itemImageResouceId);
        if (mModel.getRecipe().getIngredientsAndQuantities().size() == 0) {
            mCraftButton.setEnabled(false);
        }

        if (Locale.getDefault().getLanguage().equals(Locale.FRENCH.getLanguage()) && mModel.isFrenchFeminineGender()) {
            mDroppedByLabel.setText(R.string.inventory_item_dropped_by_feminine_gender);
        }

        String stringDroppedBy = mContext.getString(R.string.inventory_item_can_t_be_dropped);
        final SparseIntArray lootsAndPercents = mModel.getDroppedBy().getMonstersAndPercents();
        final int lootsAndPercentsSize = lootsAndPercents.size();
        if (lootsAndPercentsSize != 0) {
            stringDroppedBy = "";
            for (int i = 0; i < lootsAndPercentsSize; i++) {
                stringDroppedBy += mContext.getString(R.string.dropped_by_entry,
                        mContext.getString(lootsAndPercents.keyAt(i)), lootsAndPercents.valueAt(i));
                stringDroppedBy += " ";
            }
            stringDroppedBy = stringDroppedBy.substring(0, stringDroppedBy.length() - 2);
        }
        mDroppedBy.setText(stringDroppedBy);
    }

    public void setCraftRequestListener(final InventoryCraftListener listener) {
        mCraftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCraftRequested(mModel);
            }
        });
    }
}
