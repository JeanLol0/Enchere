/*
 * Copyright 2023 jules.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.insa.strasbourg.beuvron.utils;

import javafx.scene.Scene;

/**
 *
 * @author jules
 */
public class SceneManager{
    private static SceneManager instance = new SceneManager();
    private static Scene scene;

    private SceneManager() {}

    public static SceneManager getInstance() {
        return instance;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public static Scene getScene() {
        return scene;
    }
}