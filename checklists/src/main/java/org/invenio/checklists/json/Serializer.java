package org.invenio.checklists.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.Collection;
import org.invenio.checklists.util.ApplicationConfigPropertiesUtils;

/**
 * Abstract serializer with the logic to serialize any object. Uses builder capabilities
 * to decide which attributes of the object to serialize.
 * 
 * All serializer classes can not be part of Spring's container, since
 * we use the builder capabilities of them when creating the objects on demand.
 * 
 * @author avillalobos
 */
public abstract class Serializer {
    
    private final Gson gson;
    
    private boolean addSimple;
    private boolean addFull;
    private boolean addCollections;
    
    protected final SimpleDateFormat sdf;
    
    {
        ApplicationConfigPropertiesUtils properties = ApplicationConfigPropertiesUtils.getInstance();
        gson = new Gson();
        sdf = new SimpleDateFormat(properties.getProperty("date.default.format"));
    }
    
    /*
    ****************************************************************************
    Builder capabilities
    ****************************************************************************
     */
    public final Serializer addSimple() {
        this.addSimple = true;
        return this;
    }
    
    public final Serializer addFull() {
        this.addFull = true;
        return this;
    }
    
    public final Serializer addCollections() {
        this.addCollections = true;
        return this;
    }
    
    /**
     * Returns the string representation of the object passed in as a Json string.
     * 
     * @param object
     * @return 
     */
    public final String serialize(Object object) {
        
        JsonObject objectJson = serializeObject(object);
        
        String theJson = gson.toJson(objectJson);
        
        return theJson;
    }
    
    /**
     * Serializes a collection collection to JSON array.
     *
     * @param objects
     * @return
     */
    public final String serializeCollection(Collection objects) {

        JsonArray jsonArray = new JsonArray();

        for (Object object : objects) {

            JsonObject json = serializeObject(object);

            jsonArray.add(json);
        }

        String theJson = gson.toJson(jsonArray);

        return theJson;
    }
    
    /**
     * Makes the serialization using Gson.
     * 
     * @param object
     * @return the parsed object.
     */
    private JsonObject serializeObject(Object object) {
        
        JsonObject json = new JsonObject();
        
        if (addSimple) {
            addSimple(object, json);
        }
        
        if (addFull) {
            addFull(object, json);
        }
        
        if (addCollections) {
            addCollections(object, json);
        }
        
        return json;
    }
    
    /**
     * Adds the id and name (if any) of the object passed by.
     * 
     * @param object 
     * @param json 
     */
    protected abstract void addSimple(Object object, JsonObject json);
    
    /**
     * Adds all the attributes of the object passed by.
     * 
     * @param object 
     * @param json 
     */
    protected abstract void addFull(Object object, JsonObject json);
    
    /**
     * Adds all the collections of the object passed by.
     * 
     * @param object 
     * @param json 
     */
    protected abstract void addCollections(Object object, JsonObject json);

}
