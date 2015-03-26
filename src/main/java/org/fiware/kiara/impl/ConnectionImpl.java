package org.fiware.kiara.impl;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.fiware.kiara.client.Connection;
import org.fiware.kiara.dynamic.services.DynamicProxy;
import org.fiware.kiara.exceptions.ConnectException;
import org.fiware.kiara.exceptions.impl.InvalidAddressException;
import org.fiware.kiara.serialization.Serializer;
import org.fiware.kiara.transport.Transport;
import org.fiware.kiara.transport.impl.TransportImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eprosima.idl.parser.exception.ParseException;
import com.eprosima.idl.parser.grammar.KIARAIDLLexer;
import com.eprosima.idl.parser.grammar.KIARAIDLParser;
import com.eprosima.idl.parser.tree.Definition;
import com.eprosima.idl.parser.tree.Specification;
import com.eprosima.idl.util.Util;
import com.eprosima.log.ColorMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ConnectionImpl implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionImpl.class);
    private final Serializer serializer;
    private final Transport transport;
    
    ArrayList<DynamicProxy> m_dynamicServices;
    
    public ConnectionImpl(Transport transport, Serializer serializer, boolean dummy) {
        super();
        this.transport = transport;
        this.serializer = serializer;
        
        // Download and parse IDL
        if (dummy) { // TODO: Delete this and detect IDL loading by URI
            this.m_dynamicServices = new ArrayList<DynamicProxy>();
            loadIDL();
        }
        
    }
    
    public Transport getTransport() {
        return transport;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public <T> T getServiceProxy(Class<T> interfaceClass) throws Exception {
        // name of the interface class can end with 'Async' or 'Client'
        String interfaceName = interfaceClass.getName();
        if (interfaceName.endsWith("Async"))
            interfaceName = interfaceName.substring(0, interfaceName.length()-5);
        else if (interfaceName.endsWith("Client"))
            interfaceName = interfaceName.substring(0, interfaceName.length()-6);
        final String proxyClassName = interfaceName+"Proxy";
        Class<?> proxyClass = Class.forName(proxyClassName);
        if (!interfaceClass.isAssignableFrom(proxyClass))
            throw new RuntimeException("Proxy class "+proxyClass+" does not implement interface "+interfaceClass);
        Constructor<?> proxyConstr = proxyClass.getConstructor(Serializer.class, Transport.class);
        proxyConstr.setAccessible(true);
        return interfaceClass.cast(proxyConstr.newInstance(serializer, transport));
    }
    
    public DynamicProxy getDynamicProxy(String name) {
        for (DynamicProxy service : this.m_dynamicServices) {
            if (service.getServiceName().equals(name)) {
                return service;
            }
        }
        return null;
    }
    
    private void loadIDL() {
        downloadIDL();
        
        parseIDL();
    }
    
    private void downloadIDL() {
        // Not yet implemented
    }
    
    private void parseIDL() {
        
        String idlFilename = "resources/generictype.idl";
        
        String idlParseFileName = idlFilename;
        String onlyFileName = Util.getIDLFileNameOnly(idlFilename);
        
        if (idlFilename.startsWith("."+File.separator) || idlFilename.startsWith("./") || idlFilename.startsWith("./")) {
                idlFilename = idlFilename.substring(2, idlFilename.length());
        }
        
        if (idlParseFileName != null) {
                
            ParserContextImpl ctx = new ParserContextImpl(onlyFileName, idlFilename, new ArrayList<String>());
            
            try {
               
                ANTLRFileStream input = loadIDLFromFile("resources/generictype.idl");
                KIARAIDLLexer lexer = new KIARAIDLLexer(input);
                lexer.setContext(ctx);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                KIARAIDLParser parser = new KIARAIDLParser(tokens);
                // Select handling strategy for errors
                parser.setErrorHandler(new ParserExceptionErrorStrategyImpl());
                // Pass the finelame without the extension
                Specification specification = parser.specification(ctx, null, null).spec;
            
            } catch (FileNotFoundException ex) {
                System.out.println(ColorMessage.error("FileNotFoundException") + "The File " + "resources/generictype.idl" + " was not found.");
            } catch(ParseCancellationException ex) {
                    System.out.println(ColorMessage.error("ParseCancellationException") + "The File " + "resources/generictype.idl" + " cannot be parsed.");
                    System.out.println("");
            } catch (ParseException ex) { 
                    System.out.println(ColorMessage.error("ParseException") + ex.getMessage());
            } catch (RecognitionException ex) { 
                        System.out.println(ColorMessage.error("RecognitionException") + ex.getMessage());
            } catch (Exception ex) {
                    System.out.println(ColorMessage.error("Exception") + ex.getMessage());
            }
            
            this.m_dynamicServices = TypeMapper.processTree(ctx, this.serializer, this.transport);
        
        }
        
        System.out.println("");
    }
    
    private ANTLRFileStream loadIDLFromFile(String fileName) throws IOException {
        return new ANTLRFileStream(fileName);
    }
    
    
}
