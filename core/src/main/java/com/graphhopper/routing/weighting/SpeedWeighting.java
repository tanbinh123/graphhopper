/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.graphhopper.routing.weighting;

import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.util.EdgeIteratorState;

public class SpeedWeighting implements Weighting {
    private final DecimalEncodedValue speedEnc;
    private final TurnCostProvider turnCostProvider;
    private final double maxSpeed;

    public SpeedWeighting(DecimalEncodedValue speedEnc, TurnCostProvider turnCostProvider) {
        this.speedEnc = speedEnc;
        this.maxSpeed = speedEnc.getMaxDecimal();
        this.turnCostProvider = turnCostProvider;
    }

    @Override
    public double getMinWeight(double distance) {
        return 1000 * distance / (maxSpeed / 3.6);
    }

    @Override
    public boolean edgeHasNoAccess(EdgeIteratorState edgeState, boolean reverse) {
        return false;
    }

    @Override
    public double calcEdgeWeight(EdgeIteratorState edgeState, boolean reverse) {
        return calcEdgeMillis(edgeState, reverse) / 1000.0;
    }

    @Override
    public long calcEdgeMillis(EdgeIteratorState edgeState, boolean reverse) {
        double speed = (reverse ? edgeState.getReverse(speedEnc) : edgeState.get(speedEnc));
        return Math.round(3600 * edgeState.getDistance() / speed);
    }

    @Override
    public double calcTurnWeight(int inEdge, int viaNode, int outEdge) {
        return turnCostProvider.calcTurnWeight(inEdge, viaNode, outEdge);
    }

    @Override
    public long calcTurnMillis(int inEdge, int viaNode, int outEdge) {
        return turnCostProvider.calcTurnMillis(inEdge, viaNode, outEdge);
    }

    @Override
    public boolean hasTurnCosts() {
        return turnCostProvider != TurnCostProvider.NO_TURN_COST_PROVIDER;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("This method should be removed");
    }
}