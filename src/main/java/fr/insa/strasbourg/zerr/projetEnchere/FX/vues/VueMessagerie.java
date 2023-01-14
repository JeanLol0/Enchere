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
package fr.insa.strasbourg.zerr.projetEnchere.FX.vues;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author jules
 */
public class VueMessagerie extends ScrollPane {
    private FenetrePrincipale main;
    private GridPane grid;

    public VueMessagerie(FenetrePrincipale main) {
        this.main = main;
        this.grid= new GridPane();
        this.grid.add(new Label("Vue messagerie"), 0, 0);
        this.setContent(this.grid);
    }
    
}
