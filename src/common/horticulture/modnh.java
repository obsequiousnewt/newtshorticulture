package horticulture;

import horticulture.mill.BlockMillstones;
import horticulture.mill.TileEntityMillstones;
import horticulture.trees.BlockFruitLeaves;
import horticulture.trees.BlockFruitLog;
import horticulture.trees.BlockFruitSapling;
import horticulture.trees.GeneratorFruitTrees;
import horticulture.trees.ItemBlockFruitLeaves;
import horticulture.trees.ItemBlockFruitSapling;
import horticulture.trees.ItemExplodingLemon;
import horticulture.trees.ItemFruit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import universalelectricity.UniversalElectricity;
import universalelectricity.extend.CommonProxy;
import universalelectricity.recipe.RecipeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * 
 * @author ObsequiousNewt
 */
@Mod(modid=modnh.modid,name=modnh.shortName,version=modnh.version,dependencies="after:BasicComponenets")
@NetworkMod(clientSideRequired=true,serverSideRequired=false)
public final class modnh{
	//First, a few constants that have little/no effect on the code.
	public static final String modid = "horticulture";
	public static final String shortName = "Newt's Horticulture";
	public static final String version = "Aleph Two";
	public static final String UEVersionRequired = "0.6.0";
	
	@Instance
	public static modnh instance;
	@SidedProxy(clientSide="horticulture.NHClientProxy")
	public static CommonProxy proxy;
    public static final Configuration config = new Configuration(new File("config/UniversalElectricity/NewtsHorticulture.cfg"));
    public static final NHCraftingHandler craftingHandler = new NHCraftingHandler();
	
	//Items and Blocks. NH uses 0x4d00-e00 for items (19712-968) and 0x4c0-500 for blocks (1216-80)
	public static final Item itemChisel = new ItemChisel(getItemID("itemChisel",0x4d00));
	public static final Item itemFruit = new ItemFruit(getItemID("itemFruit",0x4d01));
	public static final Item itemExplodingLemon = new ItemExplodingLemon(getItemID("itemExplodingLemon",0x4d02));
	public static final Item itemFlour = new ItemNH(getItemID("itemFlour",0x4d03),"Flour").setIconCoord(2,0);
	public static final BlockMillstones blockMillstones = new BlockMillstones(getBlockID("blockMillstones",0x4c0));
	public static final BlockFruitLeaves blockFruitLeaves = new BlockFruitLeaves(getBlockID("blockFruitLeaves",0x4c1));
	public static final BlockFruitLeaves blockFruitLeaves2 = new BlockFruitLeaves(getBlockID("blockFruitLeaves2",0x4c2));
	public static final BlockFruitSapling blockFruitSapling = new BlockFruitSapling(getBlockID("blockFruitSapling",0x4c3));
	public static final BlockFruitSapling blockFruitSapling2 = new BlockFruitSapling(getBlockID("blockFruitSapling2",0x4c4));
	public static final BlockFruitLog blockFruitLog = new BlockFruitLog(getBlockID("blockFruitLog",0x4c5));
	
	public static final String[] treeFruits = {"Apple","Pear","Apricot","Cherry","Peach","Plum","Mulberry","Date","Fig","Pomegranate","Kumquat","Lemon","Lime","Orange","Banana","Coconut","Mango","Olive","Pecan"};//,"res5","res6","res7","res8","res9","res10","res11","res12","res13","res14","res15","res16"};
	public static final String[] vineFruits = {"Grape","Honeydew","Vanilla","Cantaloupe"};
	public static final String[] flowerFruits = {"Strawberry","Peanut","Allspice","Blackberry","Blueberry","Cranberry","Pineapple"};
	public static final Object[] allFruits;
	
	static{
		ArrayList<String> q = new ArrayList<String>();
		Collections.addAll(q, treeFruits);
		Collections.addAll(q, vineFruits);
		Collections.addAll(q, flowerFruits);
		allFruits = q.toArray();
		blockFruitLeaves2.setDisplaced();
		blockFruitSapling2.setDisplaced();
	}
	
	@Init
	public void load(FMLInitializationEvent event){
		proxy.init();
		UniversalElectricity.registerMod(this, shortName, UEVersionRequired);
		RecipeManager.addRecipe(itemChisel, new Object[]{"!","\"",'!',Item.stick,'"',Item.ingotIron});
		RecipeManager.addShapelessRecipe(blockMillstones,new Object[]{itemChisel,Block.stoneSingleSlab,Block.stoneSingleSlab});
		RecipeManager.addShapelessRecipe(blockFruitLeaves,new Object[]{itemChisel});
		RecipeManager.addShapelessRecipe(itemExplodingLemon, new Object[]{Block.tnt,new ItemStack(itemFruit,1,11)});
		RecipeManager.addShapelessRecipe(new ItemStack(Block.planks,2,0),new Object[]{blockFruitLog});
		GameRegistry.registerCraftingHandler(craftingHandler);
		GameRegistry.registerBlock(blockMillstones);
		GameRegistry.registerBlock(blockFruitLeaves, ItemBlockFruitLeaves.class);
		GameRegistry.registerBlock(blockFruitLeaves2, ItemBlockFruitLeaves.class);
		GameRegistry.registerBlock(blockFruitSapling, ItemBlockFruitSapling.class);
		GameRegistry.registerBlock(blockFruitSapling2, ItemBlockFruitSapling.class);
		GameRegistry.registerBlock(blockFruitLog);
		GameRegistry.registerTileEntity(TileEntityMillstones.class, "TileEntityMillstones");
		GameRegistry.registerWorldGenerator(new GeneratorFruitTrees());
		LanguageRegistry.addName(itemExplodingLemon, "Lemon");
		LanguageRegistry.addName(blockMillstones, "Millstones");
		NetworkRegistry.instance().registerGuiHandler(this, this.proxy);
		for(int i=0;i<16;++i){
			LanguageRegistry.addName(new ItemStack(blockFruitLeaves,1,i), treeFruits[i]+" Tree Branch");
			LanguageRegistry.addName(new ItemStack(blockFruitSapling,1,i), treeFruits[i]+" Tree Sapling");
		}
		for(int i=16;i<treeFruits.length;++i){
			LanguageRegistry.addName(new ItemStack(blockFruitLeaves2,1,i-16), treeFruits[i]+" Tree Branch");
			LanguageRegistry.addName(new ItemStack(blockFruitSapling2,1,i-16), treeFruits[i]+" Tree Sapling");
		}
		for(int i=0;i<allFruits.length;++i){
			LanguageRegistry.addName(new ItemStack(itemFruit,1,i), (String) allFruits[i]);
		}
	}
	
	public modnh getInstance(){
		return instance;
	}
	
	public static int getItemID(String name,int def){
		return UniversalElectricity.getItemConfigID(config, name, def);
	}

	public static int getBlockID(String name,int def){
		return UniversalElectricity.getBlockConfigID(config, name, def);
	}
	
	public void takenFromCrafting(EntityPlayer p,ItemStack i,IInventory inv){
	}
}
