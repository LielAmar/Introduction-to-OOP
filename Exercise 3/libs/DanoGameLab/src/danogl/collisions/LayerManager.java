package danogl.collisions;

import danogl.GameObject;
import danogl.util.ConcatIterator;
import danogl.util.ModifiableList;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

/**
 * package-private, used in LayerManager
 * @author Dan Nirel
 */
class LayerData {
    int layerId;
    int drawOrder;
    ModifiableList<GameObject> objects = new ModifiableList<>(false);
    Set<Integer> layersThisLayerCollidesWIth = new HashSet<>();

    LayerData(int layerId) {
        this.layerId = layerId;
        this.drawOrder = layerId;
    }
}

/**
 * Responsible for the layers mechanism, which separates GameObjects to layers.
 * The layers dictate the order of rendering and allows control of which
 * layers collide with which.
 * @author Dan Nirel
 */
public class LayerManager {
    private ModifiableList<LayerData> layers = new ModifiableList<>(false);
    private boolean shouldReSortLayers = false;
    private BiConsumer<GameObject, GameObject> handlePair;

    /**
     * package-private constructor
     */
    LayerManager(BiConsumer<GameObject, GameObject> handlePairCallback) {
        this.handlePair = handlePairCallback;
        layers.add(new LayerData(Layer.STATIC_OBJECTS));
        layers.add(new LayerData(Layer.DEFAULT));
        shouldLayersCollide(Layer.STATIC_OBJECTS, Layer.DEFAULT, true);
        shouldLayersCollide(Layer.DEFAULT, Layer.DEFAULT, true);
    }

    /**
     * Returns whether two layers are set to collide.
     * @return true if both layers exist and are set to collide, false otherwise.
     */
    public boolean doLayersCollide(int layer1Id, int layer2Id) {
        var layer1Data = getLayerData(layer1Id);
        var layer2Data = getLayerData(layer2Id);
        if(layer1Data == null || layer2Data == null)
            return false;
        return layer1Data.layersThisLayerCollidesWIth.contains(layer2Id);
    }

    /**
     * Returns a layer's draw-order. This number can be compared
     * to that of another layer to know which would be rendered first
     * (smaller draw-order means being rendered first, and therefore behind).
     * @throws NoSuchElementException If the layer does not exist
     */
    public int getLayerDrawOrder(int layerId) {
        return getLayerDataThrow(layerId).drawOrder;
    }

    /**
     * Set a layer's draw-order. Setting a layer's draw-order to being
     * smaller than another means it will be rendered first (and therefore behind).
     * @throws NoSuchElementException If the layer does not exist
     */
    public void setLayerDrawOrder(int layerId, int drawOrder) {
        getLayerDataThrow(layerId).drawOrder = drawOrder;
        shouldReSortLayers = true;
    }

    /**
     * Sets whether two layers should collide.
     * @throws NoSuchElementException If any of the layers does not exist
     */
    public void shouldLayersCollide(int layer1Id, int layer2Id, boolean shouldCollide) {
        var layer1Data = getLayerDataThrow(layer1Id);
        var layer2Data = getLayerDataThrow(layer2Id);
        if(shouldCollide) {
            layer1Data.layersThisLayerCollidesWIth.add(layer2Id);
            layer2Data.layersThisLayerCollidesWIth.add(layer1Id);
        }
        else {
            layer1Data.layersThisLayerCollidesWIth.remove(layer2Id);
            layer2Data.layersThisLayerCollidesWIth.remove(layer1Id);
        }
    }

    void addGameObject(GameObject obj, int layerId) {
        var layerData = getLayerData(layerId);
        if(layerData == null) {
            layerData = new LayerData(layerId);
            layers.add(layerData);
            shouldReSortLayers = true;
        }
        layerData.objects.add(obj);
    }

    boolean isLayerEmpty(int layerId) {
        LayerData layerData = getLayerData(layerId);
        return layerData == null || layerData.objects.size() == 0;
    }

    Iterable<GameObject> objectsInLayer(int layerId) {
        return getLayerDataThrow(layerId).objects;
    }

    boolean removeGameObject(GameObject obj, int layerId) {
        LayerData layerData = getLayerData(layerId);
        if(layerData == null)
            return false;
        return layerData.objects.remove(obj);
    }

    void flushChanges() {
        layers.flushChanges();
        for(var layerData : layers)
            layerData.objects.flushChanges();
        if(!shouldReSortLayers)
            return;
        //re-sort layers
        var newLayersList = new ModifiableList<LayerData>(false);
        StreamSupport.stream(layers.spliterator(), false)
                .sorted(Comparator.comparing(layerData->layerData.drawOrder))
                .forEach(layerData->newLayersList.add(layerData));
        layers = newLayersList;
        layers.flushChanges();
        shouldReSortLayers = false;
    }

    Iterator<GameObject> iterator() {
        return new ConcatIterator<GameObject>(
                StreamSupport.stream(layers.spliterator(), false)
                .map(layerData -> (Iterable<GameObject>)layerData.objects)
                ::iterator);
    }

    Iterable<GameObject> reverseOrder() {
        return ()->new ConcatIterator<GameObject>(
               StreamSupport.stream(layers.reverseOrder().spliterator(), false)
               .map(layerData -> (Iterable<GameObject>)layerData.objects.reverseOrder())
               ::iterator);
    }

    void handleCollisions() {
        for(int i = 0 ; i < layers.size() ; i++) {
            for(int j = i ; j < layers.size() ; j++) {
                if(!layers.get(i).layersThisLayerCollidesWIth.contains(
                        layers.get(j).layerId))
                    continue;
                handleLayers(layers.get(i).objects, layers.get(j).objects);
            }
        }
    }

    private void handleLayers(
            ModifiableList<GameObject> layerObjs1, ModifiableList<GameObject> layerObjs2) {
        for(int ind1 = 0 ; ind1 < layerObjs1.size() ; ind1++) {
            int ind2 = 0;
            if(layerObjs1 == layerObjs2)
                ind2 = ind1 + 1;
            for(; ind2 < layerObjs2.size() ; ind2++) {
                if(layerObjs1.get(ind1) != layerObjs2.get(ind2))
                    handlePair.accept(layerObjs1.get(ind1), layerObjs2.get(ind2));
            }
        }
    }

    private LayerData getLayerData(int layerId) {
        return layers.findFirst(data -> data.layerId == layerId);
    }

    private LayerData getLayerDataThrow(int layerId) {
        var layerData = getLayerData(layerId);
        if(layerData == null)
            throw new NoSuchElementException(
                    String.format("Layer %d does not contain any objects", layerId));
        return layerData;
    }
}
