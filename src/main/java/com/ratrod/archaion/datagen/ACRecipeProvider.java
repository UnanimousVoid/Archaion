package com.ratrod.archaion.datagen;

import com.ratrod.archaion.Archaion;
import com.ratrod.archaion.registry.ACItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ACRecipeProvider extends RecipeProvider {
    public ACRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shapeless(RecipeCategory.COMBAT, ACItems.BRAVE_ESSENCE, 2)
                .requires(ACItems.BRAVE_ROD)
                .unlockedBy("has_brave_rod", this.has(ACItems.BRAVE_ROD))
                .save(this.output);

        this.shapeless(RecipeCategory.COMBAT, ACItems.IMPACT_PEARL)
                .requires(ACItems.BRAVE_ESSENCE)
                .requires(Items.ENDER_PEARL)
                .unlockedBy("has_brave_essence", this.has(ACItems.BRAVE_ESSENCE))
                .save(this.output);

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ACItems.ECHO_MACE_UPGRADE_SMITHING_TEMPLATE.get()),
                Ingredient.of(Items.MACE),
                Ingredient.of(Items.ECHO_SHARD),
                RecipeCategory.COMBAT,
                ACItems.ECHO_MACE.get()
        ).unlocks("has_echo_shard", this.has(Items.ECHO_SHARD)).save(this.output, Archaion.prefix("echo_mace_upgrade_smithing").toString());

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ACItems.ECHOS_GRACE_UPGRADE_SMITHING_TEMPLATE.get()),
                Ingredient.of(Items.BOW),
                Ingredient.of(ACItems.ECHO_CHARGE.get()),
                RecipeCategory.COMBAT,
                ACItems.ECHOS_GRACE.get()
        ).unlocks("has_echo_charge", this.has(ACItems.ECHO_CHARGE)).save(this.output, Archaion.prefix("echos_grace_upgrade_smithing").toString());
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ACRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Archaion Recipes";
        }
    }
}
