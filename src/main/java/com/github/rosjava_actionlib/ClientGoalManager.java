/**
 * Copyright 2015 Ekumen www.ekumenlabs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rosjava_actionlib;

import org.ros.internal.message.Message;

/**
 * Class that binds and action goal with a state machine to track its state.
 *
 * @author Ernesto Corbellini ecorbellini@ekumenlabs.com
 */
public class ClientGoalManager<T_ACTION_GOAL extends Message> {
    ActionGoal<T_ACTION_GOAL> actionGoal = null;
    ClientStateMachine stateMachine = null;

    public ClientGoalManager(ActionGoal<T_ACTION_GOAL> ag) {
        actionGoal = ag;
        stateMachine = new ClientStateMachine();
        stateMachine.setState(ClientState.ERROR);
    }

    public void setGoal(ActionGoal<T_ACTION_GOAL> ag) {
        actionGoal = ag;
        if (stateMachine != null && stateMachine.isRunning()) stateMachine.setState(-3);
        stateMachine = new ClientStateMachine();
        stateMachine.setState(ClientState.WAITING_FOR_GOAL_ACK);
    }

    public void setGoal(T_ACTION_GOAL agm) {
        ActionGoal<T_ACTION_GOAL> ag = new ActionGoal();
        ag.setActionGoalMessage(agm);
        setGoal(ag);
    }

    public boolean cancelGoal() {
        return stateMachine.cancel();
    }

    public void resultReceived() {
        stateMachine.resultReceived();
    }

    public void updateStatus(int status) {
        stateMachine.transition(status);
    }

    public int getGoalStateInteger() {
        int ret = -666;
        if (stateMachine != null) {
            ret = stateMachine.getState().getValue();
        }
        return ret;
    }

    public ClientState getGoalState() {
        ClientState ret = ClientState.UNKNOWN_STATE;
        if (stateMachine != null) {
            ret = stateMachine.getState();
        }
        return ret;
    }
}
