/*
 * Copyright (C) 2020 ArrowOS
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

package org.lineageos.settings.display;

import org.lineageos.settings.utils.FileUtils;

public final class KcalUtils {

    public static final String KCAL_ENABLE_NODE = "/sys/devices/platform/kcal_ctrl.0/kcal_enable";
    public static final String KCAL_RGB_NODE = "/sys/devices/platform/kcal_ctrl.0/kcal";

    // Write the given value to the given position on the KCAL node
    // position 1 is RED
    // position 2 is GREEN
    // position 3 is BLUE
    // RED GREEN BLUE is the format of the data that will be printed to the node

    public static void writeConfigToNode(int position, int value) {
        String mDefaultFormat = "R G B";
        String mNewNodeData = getNodeData(0);

        if (value < 1) {
            value = 1;
        }

        switch(position) {
            case 1:
                mNewNodeData = mDefaultFormat.replace("R", String.valueOf(value));
                break;
            case 2:
                mNewNodeData = mDefaultFormat.replace("G", String.valueOf(value));
                break;
            case 3:
                mNewNodeData = mDefaultFormat.replace("B", String.valueOf(value));
                break;
        }

        FileUtils.writeLine(KCAL_RGB_NODE, mNewNodeData
            .replace("R", getNodeData(1))
            .replace("G", getNodeData(2))
            .replace("B", getNodeData(3)));
    }

    // Get the value of the given position
    // 0 is the full node value
    // 1, 2 and 3 give the first, second and third value respectively
    public static String getNodeData(int position) {
        String mNodeData = FileUtils.readOneLine(KCAL_RGB_NODE);
        switch(position) {
            case 0:
                return mNodeData;
            case 1:
            case 2:
            case 3:
                return mNodeData.split(" ")[position - 1];
            default:
                return null;
        }
    }
}
