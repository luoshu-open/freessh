package org.freessh.sshclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朱小杰
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServerParent {

    /**
     * 这个是父节点，是下面 server  的父节点
     */
    private SSHServer parent;

    private SSHServer server;

}
