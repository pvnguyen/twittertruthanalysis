package analysis;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by phuong on 4/4/14.
 */
public class RetweetGraphAnalyzer {

    public static void main(String [] args) {
        // Graph<V, E> where V is the type of the vertices
        // and E is the type of the edges
        Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
        // Add some vertices. From above we defined these to be type Integer.
        g.addVertex((Integer)1);
        g.addVertex((Integer)2);
        g.addVertex((Integer)3);
        // Add some edges. From above we defined these to be of type String
        // Note that the default is for undirected edges.
        g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", 2, 3);
        // Let's see what we have. Note the nice output from the
        // SparseMultigraph<V,E> toString() method
        System.out.println("The graph g = " + g.toString());
        // Note that we can use the same nodes and edges in two different graphs.
        Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
        g2.addVertex((Integer)1);
        g2.addVertex((Integer)2);
        g2.addVertex((Integer)3);
        g2.addEdge("Edge-A", 1,3);
        g2.addEdge("Edge-B", 2,3, EdgeType.DIRECTED);
        g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
        g2.addEdge("Edge-P", 2,3); // A parallel edge
        System.out.println("The graph g2 = " + g2.toString());

        Graph<Integer, String> g3 = new DirectedSparseGraph<Integer, String>();
        g3.addVertex((Integer)1);
        g3.addVertex((Integer)2);
        g3.addVertex((Integer)3);
        g3.addVertex((Integer)3);
        g3.addEdge("Edge-A", 1, 3);
        g3.addEdge("Edge-B", 2,3, EdgeType.DIRECTED);
        g3.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
        g3.addEdge("Edge-P", 2, 3); // A parallel edge
        System.out.println("The graph g3 = " + g3.toString());

        RetweetGraphAnalyzer analyzer = new RetweetGraphAnalyzer();
        analyzer.visualizeGraph(g3);

    }

    public void visualizeGraph(Graph g) {
        // Layout<V, E>, BasicVisualizationServer<V,E>
        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300,300));
        BasicVisualizationServer<Integer,String> vv =
                new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        // Setup up a new vertex to paint transformer...
        Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                return Color.GREEN;
            } };
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<String, Stroke> edgeStrokeTransformer =
                new Transformer<String, Stroke>() {
                    public Stroke transform(String s) {
                        return edgeStroke;
                    }
                };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public void visualizeRetweetGraph(Graph g) {
        // Layout<V, E>, BasicVisualizationServer<V,E>
        Layout<TweetNode, RetweetEdge> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300,300));
        BasicVisualizationServer<TweetNode, RetweetEdge> vv =
                new BasicVisualizationServer<TweetNode, RetweetEdge>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        // Setup up a new vertex to paint transformer...
        Transformer<TweetNode, Paint> vertexPaint = new Transformer<TweetNode, Paint>() {
            public Paint transform(TweetNode i) {
//                if(i.isVerified())
//                    return Color.GREEN;
//                else
//                    return Color.RED;
                if (i.getSentiment() < 2) return Color.RED;
                else return Color.GREEN;
            } };
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<RetweetEdge, Stroke> edgeStrokeTransformer =
                new Transformer<RetweetEdge, Stroke>() {
                    public Stroke transform(RetweetEdge s) {
                        return edgeStroke;
                    }
                };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public void visualizeRetweetGraph2(Graph g) {
        // Layout<V, E>, BasicVisualizationServer<V,E>
        Layout<TweetNode, RetweetEdge> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300,300));
        BasicVisualizationServer<TweetNode, RetweetEdge> vv =
                new BasicVisualizationServer<TweetNode, RetweetEdge>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        // Setup up a new vertex to paint transformer...
        Transformer<TweetNode, Paint> vertexPaint = new Transformer<TweetNode, Paint>() {
            public Paint transform(TweetNode i) {
                if(i.isVerified())
                    return Color.GREEN;
                else
                    return Color.RED;
            } };
        // Set up a new stroke Transformer for the edges
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<RetweetEdge, Stroke> edgeStrokeTransformer =
                new Transformer<RetweetEdge, Stroke>() {
                    public Stroke transform(RetweetEdge s) {
                        return edgeStroke;
                    }
                };
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}
