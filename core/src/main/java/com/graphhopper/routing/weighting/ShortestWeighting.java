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

import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.util.EdgeIteratorState;

import static com.graphhopper.routing.weighting.TurnCostProvider.NO_TURN_COST_PROVIDER;

/**
 * Calculates the shortest route - independent of a vehicle as the calculation is based on the
 * distance only.
 * <p>
 *
 * @author Peter Karich
 */
public class ShortestWeighting extends AbstractWeighting {
    public ShortestWeighting(FlagEncoder flagEncoder) {
        this(flagEncoder, NO_TURN_COST_PROVIDER);
    }

    public ShortestWeighting(FlagEncoder flagEncoder, TurnCostProvider turnCostProvider) {
        this(flagEncoder.getAverageSpeedEnc(), flagEncoder.getAccessEnc(), turnCostProvider);
    }

    public ShortestWeighting(DecimalEncodedValue avgSpeedEnc, BooleanEncodedValue accessEnc, TurnCostProvider turnCostProvider) {
        super(avgSpeedEnc, accessEnc, turnCostProvider);
    }

    @Override
    public double getMinWeight(double currDistToGoal) {
        return currDistToGoal;
    }

    @Override
    public double calcEdgeWeight(EdgeIteratorState edgeState, boolean reverse) {
        return edgeState.getDistance();
    }

    @Override
    public String getName() {
        return "shortest";
    }
}
