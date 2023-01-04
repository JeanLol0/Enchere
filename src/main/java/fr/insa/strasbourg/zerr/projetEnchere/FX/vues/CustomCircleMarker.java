/*
 * Copyright 2023 jeanl.
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

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Affiche un point rouge sur la carte */
public class CustomCircleMarker extends MapLayer {

 private final MapPoint mapPoint;
 private final Circle circle;

 /**
  * @param mapPoint le point (latitude et longitude) où afficher le cercle
  * @see com.gluonhq.maps.MapPoint
  */
 public CustomCircleMarker(MapPoint mapPoint) {
  this.mapPoint = mapPoint;

  /* Cercle rouge de taille 5 */
  this.circle = new Circle(5, Color.RED);

  /* Ajoute le cercle au MapLayer */
  this.getChildren().add(circle);
 }

 /* La fonction est appelée à chaque rafraichissement de la carte */
 @Override
 public void layoutLayer() {
  /* Conversion du MapPoint vers Point2D */
  Point2D point2d = this.getMapPoint(mapPoint.getLatitude(), mapPoint.getLongitude());

  /* Déplace le cercle selon les coordonnées du point */
  circle.setTranslateX(point2d.getX());
  circle.setTranslateY(point2d.getY());
 }
}