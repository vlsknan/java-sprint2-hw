package ru.yandex.manager.history;

import ru.yandex.manager.history.HistoryManager;
import ru.yandex.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        Node node = nodeMap.get(task);
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> nodeList = new ArrayList<>();
        Node node = first;
        while (node != null) {
            nodeList.add(node.task);
            node = node.next;
        }
        return nodeList;
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            nodeMap.remove(id);
            removeNode(node);
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task, last, null);
        if (first == null) {
            first = newNode;
        } else {
            if (nodeMap.containsKey(task.getID())) {
                remove(task.getID());
            }
                last.next = newNode;
        }
        last = newNode;
        nodeMap.put(task.getID(), newNode);
    }

    public void removeNode(Node node) {
        Node nodePrev = node.prev;
        Node nodeNext = node.next;
        if (node == first) {
            first = first.next;
            if (first != null) {
                first.prev = null;
            }
           return;
        }
        if (node != last) {
            nodePrev.next = nodeNext;
            nodeNext.prev = nodePrev;
            node.prev = null;
            node.next = null;
        } else {
            last = last.prev;
            last.next = null;
        }
    }

    static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}
