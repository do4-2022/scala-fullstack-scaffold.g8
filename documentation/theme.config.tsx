import React, { useEffect, useState } from 'react'
import { DocsThemeConfig } from 'nextra-theme-docs'
import { useSpring, animated } from '@react-spring/web';

function Logo() {
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
    >
      <img
        style={{ borderRadius: '5px', marginRight: '10px' }}
        width={50}
        src='/scaffold_logo.png'
      ></img>
      <h1 style={{ fontWeight: 'bold', color: "#F76E57" }}>Scala Fullstack Scaffold</h1>
    </div>
  )
}

function Numbers({n}) {
  const { number } = useSpring({
    from: { number: 0 },
    number: n,
    delay: 200,
    config: { mass: 1, tension: 20, friction: 10  },
  })
  return <animated.p className='nx-text-xs'>{number.to((n) => n.toFixed(0))}</animated.p>
}


function ProjectStars() {
  const [stars, setStars] = useState(0);

  useEffect(() => {
    fetch('https://api.github.com/repos/do4-2022/scala-fullstack-scaffold')
      .then(response => response.json())
      .then(data => setStars(data.stargazers_count));
  }, []);


  return (
    <a style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    }}
      href="https://github.com/do4-2022/scala-fullstack-scaffold"
      target="_blank"
      rel="noopener noreferrer"
    >
      <div
      style={{
        borderRadius: '5px 0 0 5px',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
      }}
      className="nx-border nx-border-gray-300 dark:nx-border-gray-600 nx-py-1 nx-px-2">
      <p className='nx-text-xs'>⭐ Star</p>
      </div>
      <div 
      style={{
        borderRadius: '0 5px 5px 0',
        borderLeft: 'none',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        overflow: 'hidden',
      }}
      className="nx-border nx-border-gray-300 dark:nx-border-gray-600 nx-py-1 nx-px-2">
        <Numbers n={stars}/>
      </div>
    </a>
  )
}


const config: DocsThemeConfig = {
  logo: <Logo/>,
  project: {
    link: 'https://github.com/do4-2022/scala-fullstack-scaffold',
  },
  banner: {
    text: (
      <a
        href='https://github.com/do4-2022/scala-fullstack-scaffold/issues/new?title=Feedback%20for%20%E2%80%9Cscala-fullstack-scaffold%E2%80%9D&labels=feedback'
        target='_blank'
      >
        <strong>Scala Fullstack Scaffold's documentation is under construction.</strong> Give us
        your feedback →
      </a>
    ),
  },
  navbar: {
    extraContent: <ProjectStars/>
  },
  docsRepositoryBase: 'https://github.com/do4-2022/scala-fullstack-scaffold',
  editLink: {
    text: 'Edit this page on GitHub →',
  },
  footer: {
    text: 'MIT 2023 © Scala Fullstack Scaffold maintainers.',
  },
}

export default config
