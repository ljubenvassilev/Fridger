package ljuboandtedi.fridger.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private HashMap<String, User> users;
    private User currentUser;
    private static final String DATABASE_NAME = "Fridger.db";
    private static final String USERS_TABLE = "users_table";
    private static DatabaseHelper ourInstance;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        users = new HashMap<>();
    }

    public static DatabaseHelper getInstance(Context context){
        if (ourInstance==null){ourInstance
                =
                new DatabaseHelper(context);}
        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USERS_TABLE +" (" +
                "ID INTEGER PRIMARY KEY," +
                "VEGAN TEXT," +
                "VEGETARIAN TEXT, " +
                "PESCETARIAN TEXT, " +
                "OVOVEGETARIAN TEXT, " +
                "LACTOVEGETARIAN TEXT, " +
                "PALEO TEXT, " +

                "DAIRY TEXT, " +
                "EGG TEXT," +
                "GLUTEN TEXT, " +
                "PEANUT TEXT, " +
                "SEAFOOD TEXT, " +
                "SESAME TEXT, " +
                "SOY TEXT, " +
                "SULFITE TEXT," +
                "TREENUT TEXT, " +
                "WHEAT TEXT, " +

                "AMERICAN TEXT, " +
                "ITALIAN TEXT, " +
                "ASIAN TEXT, " +
                "MEXICAN TEXT, " +
                "SOUTHERNANDSOULFOOD TEXT," +
                "FRENCH TEXT, " +
                "SOUTHWESTERN TEXT, " +
                "BARBECUE TEXT, " +
                "INDIAN TEXT, " +
                "CHINESE TEXT, " +
                "CAJUNANDCREOLE TEXT," +
                "ENGLISH TEXT, " +
                "MEDITERRANEAN TEXT, " +
                "GREEK TEXT, " +
                "SPANISH TEXT, " +
                "GERMAN TEXT, " +
                "THAI TEXT, " +
                "MOROCCAN TEXT," +
                "IRISH TEXT, " +
                "JAPANESE TEXT, " +
                "CUBAN TEXT, " +
                "HAWAIIAN TEXT, " +
                "SWEDISH TEXT, " +
                "HUNGARIAN TEXT, " +
                "PORTUGUESE TEXT )" );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void initUsers (final String userID){
        if (!userExists(userID)) {addUser(userID);}
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USERS_TABLE,null);
        if(cursor.moveToFirst()){
            do{
                String username = cursor.getString(0);
                users.put(username,new User(username, getUserPreferences(username),
                        getUserFridge(username), getUserShoppingList(username),
                        getUserFavoriteMeals(username)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        currentUser = users.get(userID);
    }

    private void addUser(String userId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("CREATE TABLE fridge"+userId+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, LINK TEXT)");
        db.execSQL("CREATE TABLE shopinglist" + userId +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, LINK TEXT)");
        db.execSQL("CREATE TABLE favoritemeals" + userId +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, LINK TEXT)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("VEGAN", "NO");
        contentValues.put("VEGETARIAN", "NO");
        contentValues.put("PESCETARIAN", "NO");
        contentValues.put("OVOVEGETARIAN", "NO");
        contentValues.put("LACTOVEGETARIAN", "NO");
        contentValues.put("PALEO", "NO");

        contentValues.put("DAIRY", "NO");
        contentValues.put("EGG", "NO");
        contentValues.put("GLUTEN", "NO");
        contentValues.put("PEANUT", "NO");
        contentValues.put("SEAFOOD", "NO");
        contentValues.put("SESAME", "NO");
        contentValues.put("SOY", "NO");
        contentValues.put("SULFITE", "NO");
        contentValues.put("TREENUT", "NO");
        contentValues.put("WHEAT", "NO");

        contentValues.put("AMERICAN", "NO");
        contentValues.put("ITALIAN", "NO");
        contentValues.put("ASIAN", "NO");
        contentValues.put("MEXICAN", "NO");
        contentValues.put("SOUTHERNANDSOULFOOD", "NO");
        contentValues.put("FRENCH", "NO");
        contentValues.put("SOUTHWESTERN", "NO");
        contentValues.put("BARBECUE", "NO");
        contentValues.put("INDIAN", "NO");
        contentValues.put("CHINESE", "NO");
        contentValues.put("CAJUNANDCREOLE", "NO");
        contentValues.put("ENGLISH", "NO");
        contentValues.put("MEDITERRANEAN", "NO");
        contentValues.put("GREEK", "NO");
        contentValues.put("SPANISH", "NO");
        contentValues.put("GERMAN", "NO");
        contentValues.put("THAI", "NO");
        contentValues.put("MOROCCAN", "NO");
        contentValues.put("IRISH", "NO");
        contentValues.put("JAPANESE", "NO");
        contentValues.put("CUBAN", "NO");
        contentValues.put("HAWAIIAN", "NO");
        contentValues.put("SWEDISH", "NO");
        contentValues.put("HUNGARIAN", "NO");
        contentValues.put("PORTUGUESE", "NO");
        db.insert(USERS_TABLE, null, contentValues);
        users.put(userId, new User(userId,"", new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>()));
    }

    public void editUserPrefs(String userId, boolean vegan, boolean vegetarian, boolean pescetarian,
                              boolean ovovegetarian, boolean lactovegetarian, boolean paleo,
                              boolean dairy, boolean egg, boolean gluten, boolean peanut,
                              boolean seafood, boolean sesame, boolean soy, boolean sulfite,
                              boolean treenut, boolean wheat, boolean american, boolean italian,
                              boolean asian, boolean mexican, boolean southernAndSoulFood,
                              boolean french, boolean southwestern, boolean barbecue,
                              boolean indian, boolean chinese, boolean cajunAndCreole,
                              boolean english, boolean mediterranean, boolean greek,
                              boolean spanish, boolean german, boolean thai, boolean moroccan,
                              boolean irish, boolean japanese, boolean cuban, boolean hawaiian,
                              boolean swedish, boolean hungarian, boolean portuguese){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("VEGAN", yesOrNo(vegan));
        contentValues.put("VEGETARIAN", yesOrNo(vegetarian));
        contentValues.put("PESCETARIAN", yesOrNo(pescetarian));
        contentValues.put("OVOVEGETARIAN", yesOrNo(ovovegetarian));
        contentValues.put("LACTOVEGETARIAN", yesOrNo(lactovegetarian));
        contentValues.put("PALEO",yesOrNo(paleo));

        contentValues.put("DAIRY", yesOrNo(dairy));
        contentValues.put("EGG", yesOrNo(egg));
        contentValues.put("GLUTEN", yesOrNo(gluten));
        contentValues.put("PEANUT", yesOrNo(peanut));
        contentValues.put("SEAFOOD", yesOrNo(seafood));
        contentValues.put("SESAME", yesOrNo(sesame));
        contentValues.put("SOY", yesOrNo(soy));
        contentValues.put("SULFITE", yesOrNo(sulfite));
        contentValues.put("TREENUT", yesOrNo(treenut));
        contentValues.put("WHEAT", yesOrNo(wheat));

        contentValues.put("AMERICAN", yesOrNo(american));
        contentValues.put("ITALIAN", yesOrNo(italian));
        contentValues.put("ASIAN", yesOrNo(asian));
        contentValues.put("MEXICAN", yesOrNo(mexican));
        contentValues.put("SOUTHERNANDSOULFOOD", yesOrNo(southernAndSoulFood));
        contentValues.put("FRENCH", yesOrNo(french));
        contentValues.put("SOUTHWESTERN", yesOrNo(southwestern));
        contentValues.put("BARBECUE", yesOrNo(barbecue));
        contentValues.put("INDIAN", yesOrNo(indian));
        contentValues.put("CHINESE", yesOrNo(chinese));
        contentValues.put("CAJUNANDCREOLE", yesOrNo(cajunAndCreole));
        contentValues.put("ENGLISH", yesOrNo(english));
        contentValues.put("MEDITERRANEAN", yesOrNo(mediterranean));
        contentValues.put("GREEK", yesOrNo(greek));
        contentValues.put("SPANISH", yesOrNo(spanish));
        contentValues.put("GERMAN", yesOrNo(german));
        contentValues.put("THAI", yesOrNo(thai));
        contentValues.put("MOROCCAN", yesOrNo(moroccan));
        contentValues.put("IRISH", yesOrNo(irish));
        contentValues.put("JAPANESE", yesOrNo(japanese));
        contentValues.put("CUBAN", yesOrNo(cuban));
        contentValues.put("HAWAIIAN", yesOrNo(hawaiian));
        contentValues.put("SWEDISH", yesOrNo(swedish));
        contentValues.put("HUNGARIAN", yesOrNo(hungarian));
        contentValues.put("PORTUGUESE", yesOrNo(portuguese));

        db.update(USERS_TABLE, contentValues, "ID = ? ", new String[] { userId } );
        User tempUser = new User(userId,getUserPreferences(userId),getUserFridge(userId),
                getUserShoppingList(userId),getUserFavoriteMeals(userId));
        users.remove(userId);
        users.put(userId, tempUser);
    }

    private boolean userExists(String userID) {
        Cursor cursor = getWritableDatabase().rawQuery("Select * from " + USERS_TABLE +
                " where ID = " + userID, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor getUser(String userID) {
        return this.getReadableDatabase().rawQuery( "SELECT * FROM " + USERS_TABLE + " WHERE ID=?",
                new String[] { userID } );
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private String getUserPreferences (String userID) {
        String prefs="";
        Cursor res = this.getReadableDatabase().rawQuery( "SELECT * FROM " +
                USERS_TABLE + " WHERE ID=?", new String[] { userID } );
        if (res != null)
            res.moveToFirst();
        if(res.getString(1).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=386^Vegan";
        if(res.getString(2).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=387^Lacto-ovo%20vegetarian";
        if(res.getString(3).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=390^Pescetarian";
        if(res.getString(4).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=389^Ovo%20vegetarian";
        if(res.getString(5).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=388^Lacto%20vegetarian";
        if(res.getString(6).equalsIgnoreCase("YES")) prefs+="&allowedDiet[]=403^Paleo";
        if(res.getString(7).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=396^Dairy-Free";
        if(res.getString(8).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=397^Egg-Free";
        if(res.getString(9).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=393^Gluten-Free";
        if(res.getString(10).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=394^Peanut-Free";
        if(res.getString(11).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=398^Seafood-Free";
        if(res.getString(12).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=399^Sesame-Free";
        if(res.getString(13).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=400^Soy-Free";
        if(res.getString(14).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=401^Sulfite-Free";
        if(res.getString(15).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=395^Tree%20Nut-Free";
        if(res.getString(16).equalsIgnoreCase("YES")) prefs+="&allowedAllergy[]=392^Wheat-Free";
        if(res.getString(17).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-american";
        if(res.getString(18).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-italian";
        if(res.getString(19).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-asian";
        if(res.getString(20).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-mexican";
        if(res.getString(21).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-southern";
        if(res.getString(22).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-french";
        if(res.getString(23).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-southwestern";
        if(res.getString(24).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-barbecue-bbq";
        if(res.getString(25).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-indian";
        if(res.getString(26).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-chinese";
        if(res.getString(27).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-cajun";
        if(res.getString(28).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-english";
        if(res.getString(29).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-mediterranean";
        if(res.getString(30).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-greek";
        if(res.getString(31).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-spanish";
        if(res.getString(32).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-german";
        if(res.getString(33).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-thai";
        if(res.getString(34).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-moroccan";
        if(res.getString(35).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-irish";
        if(res.getString(36).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-japanese";
        if(res.getString(37).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-cuban";
        if(res.getString(38).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-hawaiian";
        if(res.getString(39).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-swedish";
        if(res.getString(40).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-hungarian";
        if(res.getString(41).equalsIgnoreCase("YES")) prefs+="&allowedCuisine[]=cuisine^cuisine-portuguese";
        res.close();
        return prefs;
    }

    private String yesOrNo (boolean attrib){
        return attrib?"YES":"NO";
    }

    public void addToFridge (String ingredient) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LINK",ingredient);
        getWritableDatabase().insert("fridge".concat(username), null, contentValues);
        temp.setFridge(getUserFridge(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public void addToShoppingList (String ingredient) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LINK",ingredient);
        getWritableDatabase().insert("shopinglist".concat(username), null, contentValues);
        temp.setShoppingList(getUserShoppingList(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public void addToFavoriteMeals (String meal) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        ContentValues contentValues = new ContentValues();
        contentValues.put("LINK",meal);
        getWritableDatabase().insert("favoritemeals".concat(username), null, contentValues);
        temp.setFavouriteMeals(getUserFavoriteMeals(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public void removeFromFridge (String ingredient) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        getWritableDatabase().delete("fridge".concat(username),"LINK=?",new String[]{ingredient});
        temp.setFridge(getUserFridge(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public void removeFromShoppingList (String ingredient) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        getWritableDatabase().delete("shopinglist".concat(username),"LINK=?",new String[]{ingredient});
        temp.setShoppingList(getUserShoppingList(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public void removeFromFavoriteMeals (String meal) {
        User temp = getCurrentUser();
        String username = temp.getFacebookID();
        getWritableDatabase().delete("favoritemeals".concat(username),"LINK=?",new String[]{meal});
        temp.setFavouriteMeals(getUserFavoriteMeals(username));
        currentUser=temp;
        users.remove(username);
        users.put(username,temp);
    }

    public ArrayList<String> getUserFridge (String username){
        ArrayList<String> newFridge = new ArrayList<>();
        Cursor res = this.getReadableDatabase().rawQuery( "SELECT * FROM fridge"+username,null);
        if (res.moveToFirst()){
           do{newFridge.add(res.getString(1)); }while (res.moveToNext());
        }
        res.close();
        return newFridge;
    }

    public ArrayList<String> getUserShoppingList (String username){
        ArrayList<String> newShoppingList = new ArrayList<>();
        Cursor res = this.getReadableDatabase().rawQuery( "SELECT * FROM shopinglist".concat(username),null);
        if (res.moveToFirst()){
            do{newShoppingList.add(res.getString(1)); }while (res.moveToNext());
        }
        res.close();
        return newShoppingList;
    }

    public ArrayList<String> getUserFavoriteMeals (String username){
        ArrayList<String> newFavoriteMeals = new ArrayList<>();
        Cursor res = this.getReadableDatabase().rawQuery( "SELECT * FROM favoritemeals".concat(username),null);
        if (res.moveToFirst()){
            do{newFavoriteMeals.add(res.getString(1)); }while (res.moveToNext());
        }
        res.close();
        return newFavoriteMeals;
    }
}
