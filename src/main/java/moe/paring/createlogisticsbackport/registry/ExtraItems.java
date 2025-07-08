package moe.paring.createlogisticsbackport.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import moe.paring.createlogisticsbackport.BuilderTransformersExtra;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageItem;
import moe.paring.createlogisticsbackport.content.logistics.box.PackageStyles;
import moe.paring.createlogisticsbackport.content.logistics.tableCloth.ShoppingListItem;
import com.tterrag.registrate.util.entry.ItemEntry;

import static moe.paring.createlogisticsbackport.CreateLogisticsBackport.REGISTRATE;

public class ExtraItems {
    static {
        Create.REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<ShoppingListItem> SHOPPING_LIST =
            REGISTRATE.item("shopping_list", ShoppingListItem::new)
                    .properties(p -> p.stacksTo(1))
                    .register();

    static {
        boolean rareCreated = false;
        boolean normalCreated = false;
        for (PackageStyles.PackageStyle style : PackageStyles.STYLES) {
            ItemBuilder<PackageItem, CreateRegistrate> packageItem = BuilderTransformersExtra.packageItem(style);

            if (rareCreated && style.rare() || normalCreated && !style.rare())
                packageItem.setData(ProviderType.LANG, NonNullBiConsumer.noop());

            rareCreated |= style.rare();
            normalCreated |= !style.rare();
            packageItem.register();
        }
    }

    public static void register() {}

}
