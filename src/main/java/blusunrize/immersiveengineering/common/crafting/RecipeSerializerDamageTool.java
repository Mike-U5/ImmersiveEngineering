/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.crafting;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;

public class RecipeSerializerDamageTool implements IRecipeSerializer<RecipeDamageTool>
{
	public static final IRecipeSerializer<RecipeDamageTool> INSTANCE = RecipeSerializers.register(
			new RecipeSerializerDamageTool()
	);

	@Nonnull
	@Override
	public RecipeDamageTool read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json)
	{
		NonNullList<Ingredient> defIngredients = readIngredients(json.getAsJsonArray("ingredients"));
		Ingredient tool = Ingredient.fromJson(json.get("tool"));
		String group = json.get("group").getAsString();
		ItemStack result = ShapedRecipe.deserializeItem(json.getAsJsonObject("result"));
		return new RecipeDamageTool(recipeId, group, result, tool, defIngredients);
	}

	@Nonnull
	@Override
	public RecipeDamageTool read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer)
	{
		int stdCount = buffer.readInt();
		NonNullList<Ingredient> stdIngr = NonNullList.create();
		for(int i = 0; i < stdCount; ++i)
		{
			stdIngr.add(Ingredient.fromBuffer(buffer));
		}
		Ingredient tool = Ingredient.fromBuffer(buffer);
		String group = buffer.readString(512);
		ItemStack output = buffer.readItemStack();
		return new RecipeDamageTool(recipeId, group, output, tool, stdIngr);
	}

	@Override
	public void write(@Nonnull PacketBuffer buffer, @Nonnull RecipeDamageTool recipe)
	{
		int standardCount = recipe.getIngredients().size()-1;
		buffer.writeInt(standardCount);
		for(int i = 0; i < standardCount; ++i)
		{
			CraftingHelper.write(buffer, recipe.getIngredients().get(i));
		}
		CraftingHelper.write(buffer, recipe.getTool());
		buffer.writeString(recipe.getGroup());
		buffer.writeItemStack(recipe.getRecipeOutput());
	}

	@Nonnull
	@Override
	public ResourceLocation getName()
	{
		return new ResourceLocation(ImmersiveEngineering.MODID, "damage_tool");
	}

	private static NonNullList<Ingredient> readIngredients(JsonArray all)
	{
		NonNullList<Ingredient> ret = NonNullList.create();

		for(int i = 0; i < all.size(); ++i)
		{
			Ingredient ingredient = Ingredient.fromJson(all.get(i));
			if(!ingredient.hasNoMatchingItems())
				ret.add(ingredient);
		}

		return ret;
	}
}